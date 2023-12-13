package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Reservation;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.User;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository.ReservationRepository;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository.UserRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends CrudServiceImplementation<User, Long> implements UserService {
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public UserServiceImpl(UserRepository userRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }


    @Override
    protected CrudRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Override
    public Collection<User> readAllByReservedCar(Long carId) {
        return reservationRepository.findByCarReservedId(carId).stream().map(Reservation::getReservationMaker).collect(Collectors.toSet());
    }

    @Override
    public Collection<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Collection<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public Collection<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }
}
