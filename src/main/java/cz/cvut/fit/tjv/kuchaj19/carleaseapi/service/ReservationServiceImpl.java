package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Reservation;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository.ReservationRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ReservationServiceImpl extends CrudServiceImplementation<Reservation, Long> implements ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    @Override
    protected CrudRepository<Reservation, Long> getRepository() {
        return reservationRepository;
    }

    @Override
    public Collection<Reservation> getAllByUser(Long userId) {
        return reservationRepository.findByReservationMakerId(userId);
    }

    @Override
    public Collection<Reservation> getAllByCar(Long carId) {
        return reservationRepository.findByCarReservedId(carId);
    }
}
