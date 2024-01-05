package cz.cvut.fit.tjv.kuchaj19.carleaseapi.controller;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Reservation;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.service.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(value = "/reservation", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {
    ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Wrong parameters"),
            @ApiResponse(responseCode = "409", description = "Given ID already exists or there is a conflict with another reservation"),
            @ApiResponse(responseCode = "200")
    })
    public Reservation create(@Valid @RequestBody Reservation data) {
        try {
            return reservationService.create(data);
        } catch(IllegalIntervalException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Wrong search parameters"),
            @ApiResponse(responseCode = "200")
    })
    public Collection<Reservation> getAllOrFilter(@RequestParam Optional<Long> user, @RequestParam Optional<Long> car) {
        if (user.isPresent() || car.isPresent()) {
            try {
                return reservationService.getFiltered(user,car);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        return (Collection<Reservation>) reservationService.readAll();
    }

    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Reservation with given ID was not found"),
            @ApiResponse(responseCode = "200")
    })
    public Reservation getById(@PathVariable Long id) {
        Optional<Reservation> found = reservationService.readById(id);
        if(found.isPresent()) {
            return found.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Reservation with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Given time frame would conflict with another reservation"),
            @ApiResponse(responseCode = "204")
    })
    public void update(@PathVariable Long id, @Valid @RequestBody Reservation data) {
        try {
            reservationService.update(id, data);
        } catch (IllegalIntervalException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (IntervalCollisionException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Reservation with given ID was not found"),
            @ApiResponse(responseCode = "204")
    })
    public void delete(@PathVariable Long id) {
        try {
            reservationService.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
