package cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    Collection<User> findByEmail(String email);
    Collection<User> findByName(String name);
    Collection<User> findByPhoneNumber(String phoneNumber);
}
