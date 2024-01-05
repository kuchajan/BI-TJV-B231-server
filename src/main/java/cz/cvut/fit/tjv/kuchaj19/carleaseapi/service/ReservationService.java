package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Reservation;

import java.util.Collection;
import java.util.Optional;

public interface ReservationService extends CrudService<Reservation,Long> {
    Collection<Reservation> getFiltered(Optional<Long> userId, Optional<Long> carId);
}
