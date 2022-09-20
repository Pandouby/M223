package ch.sid.service;

import ch.sid.model.Member;
import ch.sid.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {
    MemberRepository userRepository;

    @Autowired
    public MemberService(MemberRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity getUsers() {
        return new ResponseEntity(userRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity getUser(UUID id) {
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

    public ResponseEntity update(UUID id, Member user) {
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

    public ResponseEntity delete(UUID id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    public Optional<Member> getByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
}
