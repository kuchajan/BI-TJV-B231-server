package cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Make;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface MakeRepository extends CrudRepository<Make, Long> {
}
