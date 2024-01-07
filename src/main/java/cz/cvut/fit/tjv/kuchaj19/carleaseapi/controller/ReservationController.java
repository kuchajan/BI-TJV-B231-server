package cz.cvut.fit.tjv.kuchaj19.carleaseapi.controller;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Reservation;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.service.*;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Creates a reservation", description = "Attempts to create a reservation, failing if there's a conflict with another reservation, provided data is invalid, or given ID is duplicate")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Wrong parameters", content = @Content),
            @ApiResponse(responseCode = "409", description = "Given ID already exists or there is a conflict with another reservation", content = @Content),
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
    @Operation(summary = "Get all or filtered reservations", description = "Gets all the reservation, or, if requested, reservations from a user and/or of a car")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Wrong search parameters", content = @Content),
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
    @Operation(summary = "Get a reservation", description = "Attempts to get a specific reservation, failing if given ID was not found")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Reservation with given ID was not found", content = @Content),
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
    @Operation(summary = "Edit a reservation", description = "Attempts to edit a reservation with a given ID with given data, failing if data is invalid, id not found, or there's a conflict with other reservations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Reservation with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Given time frame would conflict with another reservation", content = @Content),
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
    @Operation(summary = "Delete a reservation", description = "Attempts to delete a reservation, failing if ID is not found")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Reservation with given ID was not found", content = @Content),
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
