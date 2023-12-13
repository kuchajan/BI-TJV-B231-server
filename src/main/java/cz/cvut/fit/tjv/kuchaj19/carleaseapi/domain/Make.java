package cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Make implements EntityWithId<Long>{
    @GeneratedValue
    @Id
    Long id;
    String name;
    Long baseRentPrice;
    @OneToMany
    Collection<Car> cars;

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

    public Long getBaseRentPrice() {
        return baseRentPrice;
    }

    public void setBaseRentPrice(Long baseRentPrice) {
        this.baseRentPrice = baseRentPrice;
    }

    public Collection<Car> getCars() {
        return cars;
    }

    public void setCars(Collection<Car> cars) {
        this.cars = cars;
    }
}
