package cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

@Entity
public class Make implements EntityWithId<Long>{
    @GeneratedValue
    @Id
    Long id;
    @NotBlank(message = "Name of car is mandatory")
    String name;
    @NotNull(message = "Base rent price is mandatory")
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
