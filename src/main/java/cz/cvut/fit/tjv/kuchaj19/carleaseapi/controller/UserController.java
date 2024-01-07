package cz.cvut.fit.tjv.kuchaj19.carleaseapi.controller;

import cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain.User;
import cz.cvut.fit.tjv.kuchaj19.carleaseapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Create a user", description = "Attempts to create a user, failing if data is invalid or duplicate id")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "User does not match specific criteria (some non-nullable property is null or does not exist if foreign key)", content = @Content),
            @ApiResponse(responseCode = "409", description = "Duplicate id", content = @Content),
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
    @Operation(summary = "Get all users", description = "Gets all the users, or filtered, if requested")
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
    @Operation(summary = "Get a specific user", description = "Attempts to get a specific user, failing if id is not found")
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
    @Operation(summary = "Edit a user", description = "Attempts to edit a user, failing if ID is not found or data is invalid")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
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
    @Operation(summary = "Delete a user", description = "Attempts to delete a user, failing if id not found or user is used in a reservation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "User with given ID was not found", content = @Content),
            @ApiResponse(responseCode = "403",description = "User is used in a reservation", content = @Content),
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
