package cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Collection;

@Entity
@Table(name = "users")
public class User implements EntityWithId<Long>{
    @Id
    @GeneratedValue
    Long id;
    @Email
    @NotBlank
    String email;
    @NotBlank
    String name;
    @Pattern(regexp = "^[0-9]{9}$")
    String phoneNumber;
    @OneToMany(fetch = FetchType.LAZY)
    Collection<Reservation> reservations;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void update(EntityWithId<Long> updateWith) {
        if (updateWith.getClass() != this.getClass()) {
            throw new RuntimeException("Cannot update this entity with a different entity");
        }
        setEmail(((User)updateWith).getEmail());
        setName(((User)updateWith).getName());
        setPhoneNumber(((User)updateWith).getPhoneNumber());
        setReservations(((User)updateWith).getReservations());
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
