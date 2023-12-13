package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import java.util.Optional;

public interface CrudService<T, ID> {
    T create(T e);
    Optional<T> readById(ID id);
    Iterable<T> readAll();
    void update(ID id, T e);
    void deleteById(ID id);
}
