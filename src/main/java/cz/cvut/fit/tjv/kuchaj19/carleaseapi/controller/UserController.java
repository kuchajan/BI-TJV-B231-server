package cz.cvut.fit.tjv.kuchaj19.carleaseapi.controller;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.User;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "User does not match specific criteria (some non-nullable property is null or does not exist if foreign key)"),
            @ApiResponse(responseCode = "409", description = "Duplicate id"),
            @ApiResponse(responseCode = "200")
    })
    public User create(@Valid @RequestBody User data) {
        try {
            return userService.create(data);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200")
    })
    public Collection<User> getAllOrFiltered(@RequestParam Optional<String> email, @RequestParam Optional<String> name, @RequestParam Optional<String> phone) {
        if(email.isPresent() || name.isPresent() || phone.isPresent()) {
            return userService.getFiltered(email, name, phone);
        }
        return (Collection<User>) userService.readAll();
    }

    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "User with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "200")
    })
    public User getById(@PathVariable Long id) {
        Optional<User> found = userService.readById(id);
        if(found.isPresent()) {
            return found.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "User with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "204")
    })
    public void update(@PathVariable Long id, @Valid @RequestBody User data) {
        try {
            userService.update(id, data);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "User with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "204")
    })
    public void delete(@PathVariable Long id) {
        try {
            userService.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
