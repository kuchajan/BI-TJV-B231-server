package cz.cvut.fit.tjv.kuchaj19.carleaseapi.controller;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.Make;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.service.MakeService;
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
@RequestMapping(value = "/make", produces = MediaType.APPLICATION_JSON_VALUE)
public class MakeController {
    MakeService makeService;

    public MakeController(MakeService makeService) {
        this.makeService = makeService;
    }

    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Property violation"),
            @ApiResponse(responseCode = "409", description = "Duplicate id", content = @Content),
            @ApiResponse(responseCode = "200")
    })
    public Make create(@Valid @RequestBody Make data) {
        try {
            return makeService.create(data);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200")
    })
    public Collection<Make> getAll() {
        return (Collection<Make>)makeService.readAll();
    }

    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Make with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "200")
    })
    public Make getById(@PathVariable Long id) {
        Optional<Make> found = makeService.readById(id);
        if(found.isPresent()) {
            return found.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Make with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "204")
    })
    public void update(@PathVariable Long id, @Valid @RequestBody Make data) {
        try {
            makeService.update(id, data);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Make with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "204")
    })
    public void delete(@PathVariable Long id) {
       try {
            makeService.deleteById(id);
       }
       catch(IllegalArgumentException e) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND);
       }
       catch (DataIntegrityViolationException e) {
           throw new ResponseStatusException(HttpStatus.FORBIDDEN);
       }
    }
}
