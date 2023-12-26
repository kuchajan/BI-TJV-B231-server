package cz.cvut.fit.tjv.kuchaj19.carleaseapi.controller;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Feature;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.service.FeatureService;
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
@RequestMapping(value = "/feature", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeatureController {
    FeatureService featureService;

    public FeatureController(FeatureService featureService) {
        this.featureService = featureService;
    }

    @PostMapping
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
    @ApiResponses({
           @ApiResponse(responseCode = "200")
    })
    public Collection<Feature> getAll() {
        return (Collection<Feature>)featureService.readAll();
    }

    @GetMapping("/{id}")
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Feature with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "204")
    })
    public void delete(@PathVariable Long id) {
        try {
            featureService.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
