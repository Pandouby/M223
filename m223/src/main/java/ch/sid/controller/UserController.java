package ch.sid.controller;

import ch.sid.model.Member;
import ch.sid.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    MemberService userService;
    @Autowired
    public UserController(MemberService userService) {
        this.userService = userService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity getUsers() {
        return userService.getUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Member> getUserById(@PathVariable UUID id){
        return userService.getUser(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity createUser(@RequestBody Member user) {
        return userService.create(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable UUID id, @RequestBody Member user) {
        return userService.update(id, user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable UUID id) {
        return userService.delete(id);
    }

}
