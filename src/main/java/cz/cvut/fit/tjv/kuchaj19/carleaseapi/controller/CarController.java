package cz.cvut.fit.tjv.kuchaj19.carleaseapi.controller;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Car;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping(value="/car", produces = MediaType.APPLICATION_JSON_VALUE)
public class CarController {
    private final CarService carService;
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    @Operation(summary = "Creates a new car entity", description = "Attempts to create a car with given data about it, fails if duplicate or invalid data")
    @ApiResponses({
            @ApiResponse(responseCode = "409", description = "Duplicate id", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
            @ApiResponse(responseCode = "200")
    })
    public Car create(@Valid @RequestBody Car data) {
        try {
            return carService.create(data);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @Operation(summary = "Gets a collection of the cars", description = "Returns a collection of cars, adhering to filter specifications if any are present")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Provided only timeStart or timeEnd, but not both", content = @Content),
            @ApiResponse(responseCode = "200")
    })
    public Collection<Car> getAllOrFiltered(@RequestParam Optional<List<Long>> makeIds, @RequestParam Optional<List<Long>> featureIds, @RequestParam Optional<Long> minPrice, @RequestParam Optional<Long> maxPrice, @RequestParam Optional<Long> timeStart, @RequestParam Optional<Long> timeEnd) {
        if(makeIds.isEmpty() && featureIds.isEmpty() && minPrice.isEmpty() && maxPrice.isEmpty() && timeStart.isEmpty() && timeEnd.isEmpty()) {
            return (Collection<Car>)carService.readAll();
        }
        try {
            return carService.readAllWithFilters(makeIds.orElseGet(ArrayList::new), featureIds.orElseGet(ArrayList::new),
                    minPrice.orElse(Long.MIN_VALUE), maxPrice.orElse(Long.MAX_VALUE),
                    timeStart, timeEnd);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets a specific car", description = "Attempts to get a car, failing if given ID is not found")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Car with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "200")
    })
    public Car getById(@PathVariable Long id) {
        Optional<Car> found = carService.readById(id);
        if(found.isPresent()) {
            return found.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a car", description = "Attempts to update a car with given data, failing if data invalid or given ID is not found")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Car with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
            @ApiResponse(responseCode = "204")
    })
    public void update(@PathVariable Long id, @Valid @RequestBody Car data) {
        try {
            carService.update(id, data);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a car", description = "Attempts to delete a car, failing if any reservations are dependent on the car, or given ID id not found")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Car with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Cannot delete car, as it's a part of a reservation", content = @Content),
            @ApiResponse(responseCode = "204", content = @Content)
    })
    public void delete(@PathVariable Long id) {
        try {
            carService.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
