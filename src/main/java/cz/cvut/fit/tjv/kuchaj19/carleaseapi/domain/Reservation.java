package cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

@Entity
public class Reservation implements EntityWithId<Long>{
    @GeneratedValue
    @Id
    Long id;
    @NotNull
    Long timeStart;
    @NotNull
    Long timeEnd;
    @ManyToOne(optional = false)
    User reservationMaker;
    @ManyToOne(optional = false)
    Car carReserved;
    @AssertTrue
    Boolean validateDate() {
        return timeStart < timeEnd;
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
}
