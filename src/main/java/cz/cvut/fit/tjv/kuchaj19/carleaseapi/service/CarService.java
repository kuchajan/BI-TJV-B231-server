package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Car;

import java.util.Collection;

public interface CarService extends CrudService<Car, Long> {
    Collection<Car> readAllWithFilters(Collection<Long> makeIds, Collection<Long> featureIds, Long minPrice, Long maxPrice, Boolean availability, Long timeStart, Long timeEnd);
    Collection<Car> findAvailable(Long timeStart, Long timeEnd);
    Collection<Car> readAllByUserReserving(Long userId);
    Collection<Car> readByRegistrationPlate(String registrationPlate);
}
