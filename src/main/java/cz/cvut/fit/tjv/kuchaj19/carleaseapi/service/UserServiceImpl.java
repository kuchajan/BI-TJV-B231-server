package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Reservation;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.User;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository.ReservationRepository;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository.UserRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends CrudServiceImplementation<User, Long> implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected CrudRepository<User, Long> getRepository() {
        return userRepository;
    }
    @Override
    public Collection<User> getFiltered(Optional<String> email, Optional<String> name, Optional<String> phone) {
        Collection<User> filtered = (Collection<User>) readAll();
        email.ifPresent(s -> filtered.retainAll(userRepository.findByEmail(s)));
        name.ifPresent(s -> filtered.retainAll(userRepository.findByName(s)));
        phone.ifPresent(s -> filtered.retainAll(userRepository.findByPhoneNumber(s)));
        return filtered;
    }
}
