package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService extends CrudService<User, Long>{
    Collection<User> getFiltered(Optional<String> email, Optional<String> name, Optional<String> phone);
}
