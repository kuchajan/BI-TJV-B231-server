package cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    Collection<Reservation> findByCarReservedId(Long carId);
    Collection<Reservation> findByReservationMakerId(Long userId);
    @Query(value = "SELECT r FROM Reservation r WHERE (:timeStart BETWEEN r.timeStart AND r.timeEnd) OR (:timeEnd BETWEEN r.timeStart AND r.timeEnd) OR (r.timeStart BETWEEN :timeStart AND :timeEnd) OR (r.timeEnd BETWEEN :timeStart AND :timeEnd)")
    Collection<Reservation> findByTimeIntervalIntersect(Long timeStart, Long timeEnd);
}
