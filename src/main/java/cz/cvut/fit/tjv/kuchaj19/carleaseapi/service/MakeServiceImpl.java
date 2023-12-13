package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Make;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository.MakeRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MakeServiceImpl extends CrudServiceImplementation<Make,Long> implements MakeService {
    private final MakeRepository makeRepository;

    public MakeServiceImpl(MakeRepository makeRepository) {
        this.makeRepository = makeRepository;
    }

    @Override
    protected CrudRepository<Make, Long> getRepository() {
        return makeRepository;
    }
}
