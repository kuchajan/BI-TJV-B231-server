package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.EntityWithId;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public abstract class CrudServiceImplementation<T extends EntityWithId<ID>, ID> implements CrudService<T,ID> {
    protected abstract CrudRepository<T, ID> getRepository();
    @Override
    public T create(T e) {
        if (getRepository().existsById(e.getId()))
            throw new IllegalArgumentException();
        return getRepository().save(e);
    }

    @Override
    public Optional<T> readById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    public Iterable<T> readAll() {
        return getRepository().findAll();
    }

    @Override
    public void update(ID id, T e) {
        if (!getRepository().existsById(id)) {
            throw new IllegalArgumentException();
        }
        getRepository().save(e);
    }

    @Override
    public void deleteById(ID id) {
        getRepository().deleteById(id);
    }
}
