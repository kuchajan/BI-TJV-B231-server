package cz.cvut.fit.tjv.kuchaj19.carleaseapi.controller;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Car;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Make;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.service.CarService;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.service.MakeService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;
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
    @ApiResponses({
            @ApiResponse(responseCode = "409", description = "Duplicate id", content = @Content),
            @ApiResponse(responseCode = "200")
    })
    @Transactional
    public Car create(@Valid @RequestBody Car data) {
        try {
            return carService.create(data);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } catch (JpaObjectRetrievalFailureException e) { // thrown when make of car is invalid
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Provided only timeStart or timeEnd, but not both"),
            @ApiResponse(responseCode = "200")
    })
    public Collection<Car> getAllOrFiltered(@RequestParam Optional<List<Long>> makeIds, @RequestParam Optional<List<Long>> featureIds, @RequestParam Optional<Long> minPrice, @RequestParam Optional<Long> maxPrice, @RequestParam Optional<Long> timeStart, @RequestParam Optional<Long> timeEnd) {
        if(makeIds.isEmpty() && featureIds.isEmpty() && minPrice.isEmpty() && maxPrice.isEmpty() && timeStart.isEmpty() && timeEnd.isEmpty()) {
            return (Collection<Car>)carService.readAll();
        }
        if(timeStart.isPresent() && timeEnd.isEmpty() || timeStart.isEmpty() && timeEnd.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return carService.readAllWithFilters(makeIds.orElseGet(ArrayList::new), featureIds.orElseGet(ArrayList::new),
                minPrice.orElse(Long.MIN_VALUE), maxPrice.orElse(Long.MAX_VALUE),
                timeStart.isPresent(), timeStart.orElse(Long.MIN_VALUE), timeEnd.orElse(Long.MIN_VALUE));
    }

    @GetMapping("/{id}")
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Car with given ID was not found", content = @Content),
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Car with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "204")
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
