package cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Car;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Make;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Reservation;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.User;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collection;
import java.util.List;

@DataJpaTest
public class CarRepositoryTest {
    @Autowired
    private CarRepository carRepository; // test subject
    @Autowired
    private MakeRepository makeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void findAvailable() {
        // set up data
        Make make = new Make();
        make.setName("Test make");
        make.setBaseRentPrice(5000L);
        makeRepository.save(make);

        User user = new User();
        user.setId(1L);
        user.setEmail("test@user.com");
        user.setName("Test User");
        userRepository.save(user);

        Car available1 = new Car();
        available1.setRegistrationPlate("1A");
        available1.setMake(make);

        Reservation reservation1 = new Reservation();
        reservation1.setReservationMaker(user);
        reservation1.setCarReserved(available1);
        reservation1.setTimeStart(0L);
        reservation1.setTimeEnd(9L);

        Car available2 = new Car();
        available2.setRegistrationPlate("2A");
        available2.setMake(make);

        Reservation reservation2 = new Reservation();
        reservation2.setReservationMaker(user);
        reservation2.setCarReserved(available2);
        reservation2.setTimeStart(21L);
        reservation2.setTimeEnd(30L);

        Car unavailable1 = new Car();
        unavailable1.setRegistrationPlate("1U");
        unavailable1.setMake(make);


        Reservation reservation3 = new Reservation();
        reservation3.setReservationMaker(user);
        reservation3.setCarReserved(unavailable1);
        reservation3.setTimeStart(5L);
        reservation3.setTimeEnd(15L);

        Car unavailable2 = new Car();
        unavailable2.setRegistrationPlate("2U");
        unavailable2.setMake(make);

        Reservation reservation4 = new Reservation();
        reservation4.setReservationMaker(user);
        reservation4.setCarReserved(unavailable2);
        reservation4.setTimeStart(11L);
        reservation4.setTimeEnd(19L);

        Car unavailable3 = new Car();
        unavailable3.setRegistrationPlate("3U");
        unavailable3.setMake(make);

        Reservation reservation5 = new Reservation();
        reservation5.setReservationMaker(user);
        reservation5.setCarReserved(unavailable3);
        reservation5.setTimeStart(15L);
        reservation5.setTimeEnd(25L);

        Car unavailable4 = new Car();
        unavailable4.setRegistrationPlate("4U");
        unavailable4.setMake(make);

        Reservation reservation6 = new Reservation();
        reservation6.setReservationMaker(user);
        reservation6.setCarReserved(unavailable4);
        reservation6.setTimeStart(0L);
        reservation6.setTimeEnd(30L);

        carRepository.saveAll(List.of(available1, available2, unavailable1, unavailable2, unavailable3, unavailable4));
        reservationRepository.saveAll(List.of(reservation1,reservation2,reservation3,reservation4,reservation5, reservation6));

        // Test the method
        Collection<Car> testResult = carRepository.findAvailable(10L, 20L);

        // Assert
        Assertions.assertFalse(testResult.isEmpty());
        Assertions.assertEquals(2, testResult.size());
        Assertions.assertTrue(testResult.contains(available1));
        Assertions.assertTrue(testResult.contains(available2));
        Assertions.assertFalse(testResult.contains(unavailable1));
        Assertions.assertFalse(testResult.contains(unavailable2));
        Assertions.assertFalse(testResult.contains(unavailable3));
        Assertions.assertFalse(testResult.contains(unavailable4));
    }
}
