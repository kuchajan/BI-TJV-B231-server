package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.User;

import java.util.Collection;

public interface UserService extends CrudService<User, Long>{
    Collection<User> readAllByReservedCar(Long carId);
    Collection<User> findByEmail(String email);
    Collection<User> findByName(String name);
    Collection<User> findByPhoneNumber(String phoneNumber);
}
