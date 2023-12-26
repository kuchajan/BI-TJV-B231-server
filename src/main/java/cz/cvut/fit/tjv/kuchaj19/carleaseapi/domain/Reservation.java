package cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain;

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
    User reservationMaker;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    Car carReserved;

    public void update(EntityWithId<Long> updateWith) {
        if (updateWith.getClass() != this.getClass()) {
            throw new RuntimeException("Cannot update this entity with a different entity");
        }
        setTimeStart(((Reservation)updateWith).getTimeStart());
        setTimeEnd(((Reservation)updateWith).getTimeEnd());
        setReservationMaker(((Reservation)updateWith).getReservationMaker());
        setCarReserved(((Reservation)updateWith).getCarReserved());
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
