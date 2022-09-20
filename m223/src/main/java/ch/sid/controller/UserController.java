package ch.sid.controller;

import ch.sid.model.Member;
import ch.sid.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    MemberService userService;
    @Autowired
    public UserController(MemberService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get all Users",
            description = "Get a list of all Users",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity getUsers() {
        return userService.getUsers();
    }

    @Operation(
            summary = "Get a User by Id",
            description = "Get a User by Id",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Member> getUserById(@PathVariable UUID id){
        return userService.getUser(id);
    }

    @Operation(
            summary = "Create new User",
            description = "Create a new User",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity createUser(@RequestBody Member user) {
        return userService.create(user);
    }

    @Operation(
            summary = "Update User",
            description = "Update an existing User by Id",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable UUID id, @RequestBody Member user, @RequestHeader("Authorization") String token) throws GeneralSecurityException, IOException {
        return userService.update(id, user, token);
    }

    @Operation(
            summary = "Delete user",
            description = "Delete an existing User by Id",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable UUID id) {
        return userService.delete(id);
    }

}
