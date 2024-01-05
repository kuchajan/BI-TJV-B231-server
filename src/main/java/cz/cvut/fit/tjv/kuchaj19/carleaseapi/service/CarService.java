package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Car;

import java.util.Collection;
import java.util.Optional;

public interface CarService extends CrudService<Car, Long> {
    Collection<Car> readAllWithFilters(Collection<Long> makeIds, Collection<Long> featureIds, Long minPrice, Long maxPrice, Optional<Long> timeStart, Optional<Long> timeEnd);
}
