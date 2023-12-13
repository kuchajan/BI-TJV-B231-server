package cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "users")
public class User implements EntityWithId<Long>{
    @GeneratedValue
    @Id
    Long id;
    String email;
    String name;
    String phoneNumber;
    @OneToMany
    Collection<Reservation> reservations;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Collection<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Collection<Reservation> reservations) {
        this.reservations = reservations;
    }
}
