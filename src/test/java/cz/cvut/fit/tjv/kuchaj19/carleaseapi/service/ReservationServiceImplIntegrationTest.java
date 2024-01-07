package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.*;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@SpringBootTest
public class ReservationServiceImplIntegrationTest {
    @Autowired
    private ReservationServiceImpl reservationService; // test subject
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    private MakeRepository makeRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UserRepository userRepository;

    User user;
    Make make;
    Feature feature;
    Car car;
    Reservation reservation;

    @BeforeEach
    void setUp() {
        carRepository.deleteAll();
        featureRepository.deleteAll();
        makeRepository.deleteAll();
        reservationRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setName("Test User");
        user.setEmail("test@user.net");
        userRepository.save(user);

        make = new Make();
        make.setBaseRentPrice(5000L);
        make.setName("Test Make");
        makeRepository.save(make);

        feature = new Feature();
        feature.setPriceIncrease(500L);
        feature.setName("Test Feature");
        featureRepository.save(feature);

        car = new Car();
        car.setMake(make);
        car.setRegistrationPlate("1A");
        car.setFeatures(new HashSet<>(List.of(feature)));
        carRepository.save(car);

        reservation = new Reservation();
        reservation.setTimeStart(10L);
        reservation.setTimeEnd(20L);
        reservation.setReservationMaker(user);
        reservation.setCarReserved(car);
        reservationRepository.save(reservation);
    }

    @Test
    @Transactional
    void createTest1() {
        Reservation testReservation = new Reservation();
        testReservation.setTimeStart(25L);
        testReservation.setTimeEnd(35L);
        testReservation.setReservationMaker(user);
        testReservation.setCarReserved(car);

        testReservation = reservationService.create(testReservation);

        Assertions.assertEquals(5500L, testReservation.getPrice());
    }

    @Test
    @Transactional
    void createTest2() {
        Car anotherCar = new Car();
        anotherCar.setFeatures(new HashSet<>(List.of(feature)));
        anotherCar.setMake(make);
        anotherCar.setRegistrationPlate("2A");
        carRepository.save(anotherCar);

        Reservation testReservation = new Reservation();
        testReservation.setReservationMaker(user);
        testReservation.setCarReserved(anotherCar);
        testReservation.setTimeStart(0L);
        testReservation.setTimeEnd(86401L);

        testReservation = reservationService.create(testReservation);

        Assertions.assertEquals(11000L, testReservation.getPrice());
    }

    @Test
    @Transactional
    void createTestIntervalIntersect() {
        Reservation testReservation = new Reservation();
        testReservation.setReservationMaker(user);
        testReservation.setCarReserved(car);
        testReservation.setTimeStart(0L);
        testReservation.setTimeEnd(86401L);

        Assertions.assertThrows(IntervalCollisionException.class, () -> reservationService.create(testReservation));
    }

    @Test
    @Transactional
    void updateIntervalCollision() {
        Reservation testReservation = new Reservation();
        testReservation.setReservationMaker(user);
        testReservation.setCarReserved(car);
        testReservation.setTimeStart(30L);
        testReservation.setTimeEnd(86401L);
        reservationRepository.save(testReservation);

        Reservation newData = new Reservation();
        newData.setReservationMaker(user);
        newData.setCarReserved(car);
        newData.setTimeStart(0L);
        newData.setTimeEnd(86401L);

        Assertions.assertThrows(IntervalCollisionException.class, () -> reservationService.update(testReservation.getId(), newData));
    }

    @Test
    @Transactional
    void updateTest() {
        Reservation testReservation = new Reservation();
        testReservation.setReservationMaker(user);
        testReservation.setCarReserved(car);
        testReservation.setTimeStart(30L);
        testReservation.setTimeEnd(86401L);
        reservationRepository.save(testReservation);

        Reservation newData = new Reservation();
        newData.setReservationMaker(user);
        newData.setCarReserved(car);
        newData.setTimeStart(999L);
        newData.setTimeEnd(86401L);

        reservationService.update(testReservation.getId(), newData);
    }
}
