package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Car;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Reservation;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository.CarRepository;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository.ReservationRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ReservationServiceImpl extends CrudServiceImplementation<Reservation, Long> implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final CarRepository carRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, CarRepository carRepository) {
        this.reservationRepository = reservationRepository;
        this.carRepository = carRepository;
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

    @Override
    public Collection<Reservation> getAllByTimeIntervalIntersect(Long timeStart, Long timeEnd) {
        return reservationRepository.findByTimeIntervalIntersect(timeStart,timeEnd);
    }

    @Override
    public Reservation create(Reservation reservation) {
        // should already be with valid car id, but still check it
        Optional<Car> reserved = carRepository.findById(reservation.getCarReserved().getId());
        if(reserved.isEmpty()) {
            throw new RuntimeException("Validation failed"); //server error
        }

        if(reservationRepository.existsById(reservation.getId())) {
            // there's already a reservation with this id - conflict
            throw new IllegalArgumentException();
        }
        if(reservation.getTimeStart() >= reservation.getTimeEnd()) {
            // start of reservation should be before the end - bad request
            throw new IllegalIntervalException();
        }
        if(!carRepository.findAvailable(reservation.getTimeStart(), reservation.getTimeEnd()).contains(reserved.get())) {
            // car is not available - conflict
            throw new IntervalCollisionException();
        }
        reservationRepository.save(reservation);
        Optional<Reservation> c = reservationRepository.findById(reservation.getId());
        if(c.isEmpty()) {
            throw new RuntimeException("Created entity was not found");
        }
        return c.get();
    }

    @Override
    public void update(Long id, Reservation reservation) {
        Optional<Reservation> opt = getRepository().findById(id);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException(); // Not found
        }
        Reservation toUpdate = opt.get();
        // check special stuff
        if(reservation.getTimeStart() >= reservation.getTimeEnd()) {
            // start of reservation should be before the end - bad request
            throw new IllegalIntervalException();
        }
        // check if there's a conflict - but I am not willing to remove the old reservation - what if the server crashes mid-execution?
        Collection<Reservation> conflicts = getAllByCar(reservation.getCarReserved().getId());
        conflicts.retainAll(getAllByTimeIntervalIntersect(reservation.getTimeStart(),reservation.getTimeEnd()));
        conflicts.removeIf(x->x.getId().equals(id)); // remove this reservation from conflicts
        if(!conflicts.isEmpty()) {
            throw new IntervalCollisionException(); // the car is already reserved in another reservation - conflict
        }
        toUpdate.update(reservation);
        getRepository().save(toUpdate);
    }
}
