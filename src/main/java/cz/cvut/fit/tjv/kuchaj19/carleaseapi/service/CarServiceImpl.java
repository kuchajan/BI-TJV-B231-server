package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.*;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl extends CrudServiceImplementation<Car, Long> implements CarService {
    private final CarRepository carRepository;
    private final FeatureRepository featureRepository;
    private final MakeRepository makeRepository;

    public CarServiceImpl(CarRepository carRepository, FeatureRepository featureRepository, MakeRepository makeRepository) {
        this.carRepository = carRepository;
        this.featureRepository = featureRepository;
        this.makeRepository = makeRepository;
    }

    private Car updatePrice(Car car) {
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

    private Collection<Car> updatePrices(Collection<Car> cars) {
        return cars.stream().map(this::updatePrice).collect(Collectors.toSet());
    }

    @Override
    public Car create(Car e) {
        if (e.getId() != null && carRepository.existsById(e.getId()))
            throw new IllegalArgumentException();
        return updatePrice(carRepository.save(e));
    }

    @Override
    public Optional<Car> readById(Long id) {
        return carRepository.findById(id).map(this::updatePrice);
    }

    @Override
    public Iterable<Car> readAll() {
        return updatePrices((Collection<Car>) carRepository.findAll());
    }

    @Override
    protected CrudRepository<Car, Long> getRepository() {
        return carRepository;
    }


    @Override
    public Collection<Car> readAllWithFilters(Collection<Long> makeIds, Collection<Long> featureIds, Long minPrice, Long maxPrice, Optional<Long> timeStart, Optional<Long> timeEnd) {
        // All the cars
        Collection<Car> result = (Collection<Car>) carRepository.findAll();

        // filter out cars that aren't required makes
        if (!makeIds.isEmpty()) {
            Collection<Car> makesCar = new HashSet<Car>();
            for (Long makeId : makeIds) {
                makesCar.addAll(carRepository.findByMakeId(makeId));
            }
            result.retainAll(makesCar);
        }

        if (!featureIds.isEmpty()) {
            for (Long featureId : featureIds) {
                result.retainAll(carRepository.findByFeaturesId(featureId));
            }
        }

        if(timeStart.isPresent() && timeEnd.isEmpty() || timeStart.isEmpty() && timeEnd.isPresent()) {
            throw new IllegalArgumentException();
        }
        // filter out unavailable cars if applicable
        if (timeStart.isPresent()) {
            if(timeStart.get() >= timeEnd.get()) {
                throw new IllegalIntervalException();
            }
            result.retainAll(carRepository.findAvailable(timeStart.get(), timeEnd.get()));
        }

        // filter out cars with higher or lower price
        result = updatePrices(result);
        result.removeIf((x) -> x.getPrice() < minPrice || x.getPrice() > maxPrice);

        return result;
    }
}
