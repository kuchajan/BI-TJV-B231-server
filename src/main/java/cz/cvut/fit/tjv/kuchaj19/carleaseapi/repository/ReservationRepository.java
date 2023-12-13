package cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Reservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    Collection<Reservation> findByCarReservedId(Long carId);
    Collection<Reservation> findByReservationMakerId(Long userId);
}
