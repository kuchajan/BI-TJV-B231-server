package cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

@Entity
public class Reservation implements EntityWithId<Long>{
    @Id
    @GeneratedValue
    Long id;
    @NotNull
    Long timeStart;
    @NotNull
    Long timeEnd;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("reservations")
    User reservationMaker;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("reservations")
    Car carReserved;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long price;


    public void update(EntityWithId<Long> updateWith) {
        if (updateWith.getClass() != this.getClass()) {
            throw new RuntimeException("Cannot update this entity with a different entity");
        }
        setTimeStart(((Reservation)updateWith).getTimeStart());
        setTimeEnd(((Reservation)updateWith).getTimeEnd());
        setReservationMaker(((Reservation)updateWith).getReservationMaker());
        setCarReserved(((Reservation)updateWith).getCarReserved());
        setPrice(((Reservation)updateWith).getPrice());
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Long timeStart) {
        this.timeStart = timeStart;
    }

    public Long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public User getReservationMaker() {
        return reservationMaker;
    }

    public void setReservationMaker(User reservationMaker) {
        this.reservationMaker = reservationMaker;
    }

    public Car getCarReserved() {
        return carReserved;
    }

    public void setCarReserved(Car carReserved) {
        this.carReserved = carReserved;
    }
    public void setPrice(Long price) {
        this.price = price;
    }
    public Long getPrice() {
        return price;
    }
}
