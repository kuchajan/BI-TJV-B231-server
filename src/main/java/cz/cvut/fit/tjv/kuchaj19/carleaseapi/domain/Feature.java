package cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

@Entity
public class Feature implements EntityWithId<Long>{
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Long priceIncrease;
    @ManyToMany(mappedBy = "features")
    private Collection<Car> featureOf;


    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPriceIncrease() {
        return priceIncrease;
    }

    public void setPriceIncrease(Long priceMultiplier) {
        this.priceIncrease = priceMultiplier;
    }

    public Collection<Car> getFeatureOf() {
        return featureOf;
    }

    public void setFeatureOf(Collection<Car> isFeatureOf) {
        this.featureOf = isFeatureOf;
    }
}
