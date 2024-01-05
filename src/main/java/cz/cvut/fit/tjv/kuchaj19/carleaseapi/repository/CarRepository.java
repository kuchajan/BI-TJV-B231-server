package cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Car;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Feature;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Make;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CarRepository extends CrudRepository<Car,Long> {
    Collection<Car> findByMakeId(Long makeId);
    Collection<Car> findByFeaturesId(Long featureId);
    @Query(value = "SELECT c FROM Car c WHERE NOT EXISTS (SELECT r FROM Reservation r WHERE r.carReserved.id = c.id AND ((:timeStart BETWEEN r.timeStart AND r.timeEnd) OR (:timeEnd BETWEEN r.timeStart AND r.timeEnd)))")
    Collection<Car> findAvailable(Long timeStart, Long timeEnd);
}
