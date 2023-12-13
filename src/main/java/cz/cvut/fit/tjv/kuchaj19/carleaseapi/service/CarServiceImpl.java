package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.*;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl extends CrudServiceImplementation<Car, Long> implements CarService {
    private final CarRepository carRepository;
    //private final FeatureRepository featureRepository;
    private final ReservationRepository reservationRepository;

    public CarServiceImpl(CarRepository carRepository, /*FeatureRepository featureRepository,*/ ReservationRepository reservationRepository) {
        this.carRepository = carRepository;
        //this.featureRepository = featureRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    protected CrudRepository<Car, Long> getRepository() {
        return carRepository;
    }


    @Override
    public Collection<Car> readAllWithFilters(Collection<Long> makeIds, Collection<Long> featureIds, Double minPrice, Double maxPrice, Boolean availability, Long timeStart, Long timeEnd) {
        // All the cars or just the makes the request wants
        Collection<Car> result = !makeIds.isEmpty() ? null : (Collection<Car>) carRepository.findAll();
        if (!makeIds.isEmpty()) {
            for (Long makeId: makeIds) {
                if (result == null) {
                    result = carRepository.findByMakeId(makeId);
                } else {
                    result.addAll(carRepository.findByMakeId(makeId));
                }
            }
        }
        // filter out cars that don't have requested features
        if(!featureIds.isEmpty()) {
            // Get all the cars with the features
            Collection<Car> carsWithFeatures = null;
            for(Long featureId : featureIds) {
                if(carsWithFeatures == null) {
                    carsWithFeatures = carRepository.findByFeaturesId(featureId);
                }
                else {
                    carsWithFeatures.retainAll(carRepository.findByFeaturesId(featureId));
                }
            }
            result.retainAll(carsWithFeatures);
        }
        // filter out cars with higher or lower price
        result.removeIf((x) -> x.getPrice() < minPrice || x.getPrice() > maxPrice);
        // filter out unavailable cars if applicable
        if(availability) {
            result.retainAll(carRepository.findAvailable(timeStart,timeEnd));
        }

        return result;
    }

    @Override
    public Collection<Car> readAllByUserReserving(Long userId) {
        return reservationRepository.findByReservationMakerId(userId).stream().map(Reservation::getCarReserved).collect(Collectors.toSet());
    }

    @Override
    public Collection<Car> readByRegistrationPlate(String registrationPlate) {
        return carRepository.findByRegistrationPlate(registrationPlate);
    }
}
