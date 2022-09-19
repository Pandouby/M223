package ch.sid.service;

import ch.sid.model.Member;
import ch.sid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity getUsers() {
        return new ResponseEntity(userRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity getUser(Long id) {
        if(userRepository.existsById(id)) {
            return new ResponseEntity(userRepository.findById(id).get(), HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity create(Member user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            userRepository.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    public ResponseEntity update(Long id, Member user) {
        if(userRepository.existsById(id)) {
            Member tempUser = userRepository.findById(id).get();
            tempUser.setName(user.getName());
            tempUser.setLastname(user.getLastname());
            tempUser.setEmail(user.getEmail());
            tempUser.setPassword(user.getPassword());
            tempUser.setRole(user.getRole());
            userRepository.save(tempUser);
            return new ResponseEntity(tempUser, HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }


    }

    public ResponseEntity delete(Long id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
