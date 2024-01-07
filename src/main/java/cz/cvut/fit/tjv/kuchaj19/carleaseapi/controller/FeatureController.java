package cz.cvut.fit.tjv.kuchaj19.carleaseapi.controller;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Feature;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.service.FeatureService;
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

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(value = "/feature", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeatureController {
    FeatureService featureService;

    public FeatureController(FeatureService featureService) {
        this.featureService = featureService;
    }

    @PostMapping
    @Operation(summary = "Create a feature", description = "Attempts to create a feature from given data, fails if provided id is duplicate")
    @ApiResponses({
            @ApiResponse(responseCode = "409", description = "Duplicate id", content = @Content),
            @ApiResponse(responseCode = "200")
    })
    public Feature create(@Valid @RequestBody Feature data) {
        try {
            return featureService.create(data);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    @Operation(summary = "Gets all or filtered features", description = "Either returns all the features, or if requested, the features of a given car, or the features a given car doesn't have")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Bad filters", content = @Content),
            @ApiResponse(responseCode = "404", description = "Car with given ID not found", content = @Content)
    })
    public Collection<Feature> getAllOrFiltered(@RequestParam Optional<Long> carId, @RequestParam Optional<Boolean> inverse) {
        if(carId.isPresent()) {
            try {
                return featureService.readByCarId(carId.get(), inverse.orElse(false));
            }
            catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        return (Collection<Feature>)featureService.readAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets a specific feature", description = "Attempts to get a feature from the id")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Feature with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "200")
    })
    public Feature getById(@PathVariable Long id) {
        Optional<Feature> found = featureService.readById(id);
        if(found.isPresent()) {
            return found.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit a feature", description = "Attempts to edit a feature from the id with the data provided")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Feature with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "204")
    })
    public void update(@PathVariable Long id, @Valid @RequestBody Feature data) {
        try {
            featureService.update(id, data);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a feature", description = "Attempts to delete a feature, failing if the id was not found or if the feature is used in a car")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Feature with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Feature is used by a car", content = @Content),
            @ApiResponse(responseCode = "204")
    })
    public void delete(@PathVariable Long id) {
        try {
            featureService.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
