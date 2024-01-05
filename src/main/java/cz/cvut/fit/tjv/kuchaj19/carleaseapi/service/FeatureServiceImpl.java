package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Car;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Feature;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository.CarRepository;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.repository.FeatureRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeatureServiceImpl extends CrudServiceImplementation<Feature, Long> implements FeatureService{
    private final FeatureRepository featureRepository;
    private final CarRepository carRepository;

    public FeatureServiceImpl(FeatureRepository featureRepository, CarRepository carRepository) {
        this.featureRepository = featureRepository;
        this.carRepository = carRepository;
    }

    @Override
    protected CrudRepository<Feature, Long> getRepository() {
        return featureRepository;
    }

    @Override
    public Collection<Feature> readByCarId(Long carId, Boolean inverse) {
        if(!carRepository.existsById(carId)) {
            throw new IllegalArgumentException();
        }
        if(!inverse){
            return featureRepository.findByFeatureOfId(carId);
        }
        Collection<Feature> all = (Collection<Feature>) featureRepository.findAll();
        all.removeAll(featureRepository.findByFeatureOfId(carId));
        return all;
    }
}
