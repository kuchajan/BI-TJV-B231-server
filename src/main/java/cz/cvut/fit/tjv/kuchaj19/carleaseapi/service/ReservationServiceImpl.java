package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Car;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Feature;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Make;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Reservation;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl extends CrudServiceImplementation<Reservation, Long> implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final CarRepository carRepository;
    private final FeatureRepository featureRepository;
    private final MakeRepository makeRepository;
    private final UserRepository userRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, CarRepository carRepository, FeatureRepository featureRepository, MakeRepository makeRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.carRepository = carRepository;
        this.featureRepository = featureRepository;
        this.makeRepository = makeRepository;
        this.userRepository = userRepository;
    }

    private Car updateCarPrice(Car car) {
        Optional<Make> make = makeRepository.findById(car.getMake().getId());
        if(make.isEmpty()) {
            throw new RuntimeException();
        }
        Collection<Feature> features = featureRepository.findByFeatureOfId(car.getId());
        if(!features.isEmpty()) {
            car.setPrice(features.stream().mapToLong(Feature::getPriceIncrease).reduce(make.get().getBaseRentPrice(), Long::sum));
        }
        else {
            car.setPrice(make.get().getBaseRentPrice());
        }
        return car;
    }

    private Reservation updateReservationPrice(Reservation reservation) {
        Optional<Car> car = carRepository.findById(reservation.getCarReserved().getId());
        if(car.isEmpty()) {
            throw new RuntimeException();
        }
        Car updatedCar = updateCarPrice(car.get());
        reservation.setPrice(((long) Math.ceil((reservation.getTimeEnd() - reservation.getTimeStart()) * 1.0 / 86400.0)) * updatedCar.getPrice());
        return reservation;
    }

    private Collection<Reservation> updateReservationPrices(Collection<Reservation> reservations) {
        return reservations.stream().map(this::updateReservationPrice).collect(Collectors.toSet());
    }

    @Override
    protected CrudRepository<Reservation, Long> getRepository() {
        return reservationRepository;
    }

    @Override
    public Optional<Reservation> readById(Long id) {
        return reservationRepository.findById(id).map(this::updateReservationPrice);
    }

    @Override
    public Iterable<Reservation> readAll() {
        return updateReservationPrices((Collection<Reservation>) reservationRepository.findAll());
    }

    @Override
    public Collection<Reservation> getFiltered(Optional<Long> userId, Optional<Long> carId) {
        Collection<Reservation> all = (Collection<Reservation>) reservationRepository.findAll();
        if(userId.isPresent()) {
            if(!userRepository.existsById(userId.get())) {
                throw new IllegalArgumentException();
            }
            Collection<Reservation> what = reservationRepository.findByReservationMakerId(userId.get());
            all.retainAll(reservationRepository.findByReservationMakerId(userId.get()));
        }
        if(carId.isPresent()) {
            if(!carRepository.existsById(carId.get())) {
                throw new IllegalArgumentException();
            }
            all.retainAll(reservationRepository.findByCarReservedId(carId.get()));
        }
        return updateReservationPrices(all);
    }

    @Override
    public Reservation create(Reservation reservation) {
        if(reservation.getId() != null && reservationRepository.existsById(reservation.getId())) {
            // there's already a reservation with this id - conflict
            throw new IllegalArgumentException();
        }
        if(reservation.getTimeStart() >= reservation.getTimeEnd()) {
            // start of reservation should be before the end - bad request
            throw new IllegalIntervalException();
        }

        if(carRepository.findAvailable(reservation.getTimeStart(), reservation.getTimeEnd()).stream().mapToLong(Car::getId).noneMatch(x -> reservation.getCarReserved().getId().equals(x))) {
            // car is not available - conflict
            throw new IntervalCollisionException();
        }
        reservationRepository.save(reservation);
        Optional<Reservation> c = reservationRepository.findById(reservation.getId());
        if(c.isEmpty()) {
            throw new RuntimeException("Created entity was not found");
        }
        return updateReservationPrice(c.get());
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
        Collection<Reservation> conflicts = reservationRepository.findByCarReservedId(reservation.getCarReserved().getId());
        conflicts.retainAll(reservationRepository.findByTimeIntervalIntersect(reservation.getTimeStart(),reservation.getTimeEnd()));
        conflicts.removeIf(x->x.getId().equals(id)); // remove this reservation from conflicts
        if(!conflicts.isEmpty()) {
            throw new IntervalCollisionException(); // the car is already reserved in another reservation - conflict
        }
        toUpdate.update(reservation);
        getRepository().save(toUpdate);
    }
}
