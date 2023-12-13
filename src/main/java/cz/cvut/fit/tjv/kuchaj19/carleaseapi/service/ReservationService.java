package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Reservation;

import java.util.Collection;

public interface ReservationService extends CrudService<Reservation,Long> {
    Collection<Reservation> getAllByUser(Long userId);
    Collection<Reservation> getAllByCar(Long carId);
}
