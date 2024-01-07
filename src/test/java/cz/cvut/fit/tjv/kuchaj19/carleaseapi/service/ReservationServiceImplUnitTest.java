package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Car;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Make;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Reservation;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.User;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ReservationServiceImplUnitTest {
    @Autowired
    private ReservationServiceImpl reservationService; // test subject
    @MockBean
    private ReservationRepository reservationRepository;
    @MockBean
    private CarRepository carRepository;
    @MockBean
    private FeatureRepository featureRepository;
    @MockBean
    private MakeRepository makeRepository;
    @MockBean
    private UserRepository userRepository;
    User user;
    Make make;
    Car car;
    Reservation reservation;

    @BeforeEach
    void setUpData() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@user.com");
        user.setName("Test User");

        make = new Make();
        make.setId(1L);
        make.setName("Test make");
        make.setBaseRentPrice(5000L);

        car = new Car();
        car.setId(1L);
        car.setRegistrationPlate("1");
        car.setMake(make);
        car.setFeatures(new HashSet<>());

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setCarReserved(car);
        reservation.setReservationMaker(user);
        reservation.setTimeStart(0L);
        reservation.setTimeEnd(1L);
    }


    @Test
    void getFilteredInvalidUser() {
        // mock behaviour
        Mockito.when(reservationRepository.findAll()).thenReturn(List.of(reservation));
        Mockito.when(userRepository.existsById(2L)).thenReturn(false);

        // test method
        Assertions.assertThrows(IllegalArgumentException.class, () -> reservationService.getFiltered(Optional.of(2L),Optional.empty()));
    }

    @Test
    void getFilteredInvalidCar() {
        // mock behaviour
        Mockito.when(reservationRepository.findAll()).thenReturn(List.of(reservation));
        Mockito.when(carRepository.existsById(2L)).thenReturn(false);

        // test method
        Assertions.assertThrows(IllegalArgumentException.class, () -> reservationService.getFiltered(Optional.empty(), Optional.of(2L)));
    }

    @Test
    void getFilteredTest() {
        // prepare additional data
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Test User 2");
        user2.setEmail("test2@user.net");

        Car car2 = new Car();
        car2.setId(2L);
        car2.setRegistrationPlate("2");
        car2.setMake(make);

        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setCarReserved(car);
        reservation2.setReservationMaker(user2);

        Reservation reservation3 = new Reservation();
        reservation3.setId(3L);
        reservation3.setCarReserved(car2);
        reservation3.setReservationMaker(user);

        Reservation reservation4 = new Reservation();
        reservation4.setId(4L);
        reservation4.setCarReserved(car2);
        reservation4.setReservationMaker(user2);

        // mock behaviour for test method
        Mockito.when(reservationRepository.findAll()).thenReturn(new HashSet<>(List.of(reservation,reservation2,reservation3,reservation4)));
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(reservationRepository.findByReservationMakerId(1L)).thenReturn(new HashSet<>(List.of(reservation, reservation3)));
        Mockito.when(carRepository.existsById(1L)).thenReturn(true);
        Mockito.when(reservationRepository.findByCarReservedId(1L)).thenReturn(new HashSet<>(List.of(reservation,reservation2)));
        // mock behaviour for price calculation
        Mockito.when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        Mockito.when(makeRepository.findById(1L)).thenReturn(Optional.of(make));
        Mockito.when(featureRepository.findByFeatureOfId(1L)).thenReturn(List.of());

        // test method
        Collection<Reservation> testResult = reservationService.getFiltered(Optional.of(1L),Optional.of(1L));

        // assertions about test result
        Assertions.assertFalse(testResult.isEmpty());
        Assertions.assertEquals(1, testResult.size());
        Assertions.assertTrue(testResult.contains(reservation));
        Assertions.assertFalse(testResult.contains(reservation2));
        Assertions.assertFalse(testResult.contains(reservation3));
        Assertions.assertFalse(testResult.contains(reservation4));
        Assertions.assertEquals(5000L, reservation.getPrice());
    }

    @Test
    void createExistingId() {
        Mockito.when(reservationRepository.existsById(reservation.getId())).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> reservationService.create(reservation));
    }

    @Test
    void createIntervalInvalidLesser() {
        reservation.setTimeEnd(0L);
        reservation.setTimeStart(1L);

        Mockito.when(reservationRepository.existsById(reservation.getId())).thenReturn(false);

        Assertions.assertThrows(IllegalIntervalException.class, () -> reservationService.create(reservation));
    }

    @Test
    void createIntervalInvalidEqual() {
        reservation.setTimeStart(0L);
        reservation.setTimeEnd(0L);

        Mockito.when(reservationRepository.existsById(reservation.getId())).thenReturn(false);

        Assertions.assertThrows(IllegalIntervalException.class, () -> reservationService.create(reservation));
    }

    @Test
    void createIntervalIntersect() {
        Mockito.when(reservationRepository.existsById(reservation.getId())).thenReturn(false);
        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        Mockito.when(carRepository.findAvailable(reservation.getTimeStart(), reservation.getTimeEnd())).thenReturn(new HashSet<>());

        Assertions.assertThrows(IntervalCollisionException.class, () -> reservationService.create(reservation));
    }

    @Test
    void createTest() {
        Mockito.when(reservationRepository.existsById(reservation.getId())).thenReturn(false);
        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        Mockito.when(carRepository.findAvailable(reservation.getTimeStart(), reservation.getTimeEnd())).thenReturn(new HashSet<>(List.of(car)));
        Mockito.when(reservationRepository.save(reservation)).thenReturn(reservation);
        Mockito.when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        // mock behaviour for price calculation
        Mockito.when(makeRepository.findById(make.getId())).thenReturn(Optional.of(make));
        Mockito.when(featureRepository.findByFeatureOfId(car.getId())).thenReturn(List.of());

        reservation = reservationService.create(reservation);

        Mockito.verify(reservationRepository, Mockito.times(1)).existsById(reservation.getId());
        Mockito.verify(reservationRepository, Mockito.times(1)).save(reservation);
        Mockito.verify(reservationRepository, Mockito.times(1)).findById(reservation.getId());
        Mockito.verifyNoMoreInteractions(reservationRepository);
    }
}
