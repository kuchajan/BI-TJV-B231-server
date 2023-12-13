package cz.cvut.fit.tjv.kuchaj19.carleaseapi.service;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Feature;

import java.util.Collection;

public interface FeatureService extends CrudService<Feature,Long> {
    Collection<Feature> getAllByCar(Long carId);
}
