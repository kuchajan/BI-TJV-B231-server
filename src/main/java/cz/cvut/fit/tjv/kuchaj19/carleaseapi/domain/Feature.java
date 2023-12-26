package cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain;

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
    @ManyToMany(mappedBy = "features", fetch = FetchType.LAZY)
    private Collection<Car> featureOf;

    @Override
    public void update(EntityWithId<Long> updateWith) {
        if(updateWith.getClass() != this.getClass()) {
            throw new RuntimeException("Cannot update this entity with a different entity");
        }
        setName(((Feature)updateWith).getName());
        setPriceIncrease(((Feature)updateWith).getPriceIncrease());
        setFeatureOf(((Feature)updateWith).getFeatureOf());
    }


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
