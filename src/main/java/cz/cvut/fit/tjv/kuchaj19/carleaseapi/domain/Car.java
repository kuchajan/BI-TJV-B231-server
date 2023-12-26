package cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

@Entity
public class Car implements EntityWithId<Long>{
    @GeneratedValue
    @Id
    Long id;
    @NotBlank
    String registrationPlate;
    @NotNull
    Boolean forLease;
    @ManyToMany
    @JoinTable(
            name = "car_feature",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    Collection<Feature> features; // todo: Fix recursion when serializing json
    @OneToMany
    Collection<Reservation> reservations;
    @ManyToOne
    @NotNull
    Make make;

    @Override
    public void update(EntityWithId<Long> updateWith) {
        if(updateWith.getClass() != this.getClass()) {
            throw new RuntimeException("Cannot update this entity with a different entity");
        }
        setRegistrationPlate(((Car) updateWith).getRegistrationPlate());
        setForLease(((Car) updateWith).getForLease());
        setFeatures(((Car) updateWith).getFeatures());
        setReservations(((Car) updateWith).getReservations());
        setMake(((Car) updateWith).getMake());
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public void setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
    }

    public Boolean getForLease() {
        return forLease;
    }

    public void setForLease(Boolean forSale) {
        this.forLease = forSale;
    }

    public Collection<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Collection<Feature> features) {
        this.features = features;
    }

    public Collection<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Collection<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Make getMake() {
        return make;
    }

    public void setMake(Make make) {
        this.make = make;
    }

    public double getPrice() {
        return getMake().getBaseRentPrice() + getFeatures()
                .stream().mapToLong(Feature::getPriceIncrease).reduce(0, Long::sum);
    }
}
