package cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = "registration_plate")
)
public class Car implements EntityWithId<Long>{
    @Id
    @GeneratedValue
    Long id;
    @Column(name = "registration_plate")
    @NotBlank
    String registrationPlate;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "car_feature",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    @JsonIgnoreProperties("featureOf")
    Collection<Feature> features;
    @OneToMany(fetch = FetchType.LAZY)
    Collection<Reservation> reservations;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonIgnoreProperties("cars")
    Make make;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long price;

    @Override
    public void update(EntityWithId<Long> updateWith) {
        if(updateWith.getClass() != this.getClass()) {
            throw new RuntimeException("Cannot update this entity with a different entity");
        }
        setRegistrationPlate(((Car) updateWith).getRegistrationPlate());
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
    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getPrice() {
        return price;
    }
}
