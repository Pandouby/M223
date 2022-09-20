package ch.sid.service;

import ch.sid.model.Member;
import ch.sid.repository.MemberRepository;
import ch.sid.security.JwtServiceHMAC;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {
    MemberRepository memberRepository;
    JwtServiceHMAC jwtService;

    @Autowired
    public MemberService(MemberRepository userRepository, JwtServiceHMAC jwtService) {
        this.memberRepository = userRepository;
        this.jwtService = jwtService;
    }

    public ResponseEntity getUsers() {
        return new ResponseEntity(memberRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity getUser(UUID id) {
        if(memberRepository.existsById(id)) {
            return new ResponseEntity(memberRepository.findById(id).get(), HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity create(Member user) {
        if(memberRepository.findByEmail(user.getEmail()).isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            user.setRole("MEMBER");
            user.setId(UUID.randomUUID());
            memberRepository.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    public ResponseEntity<Member> update(UUID id, Member member, String token) throws GeneralSecurityException, IOException {
        boolean userExists = memberRepository.existsById(id);
        token = token.substring(7);
        DecodedJWT decoded = jwtService.verifyJwt(token, true);
        String user_id = decoded.getClaim("user_id").asString();
        String[] scope = decoded.getClaim("scope").asArray(String.class);
        String email = decoded.getClaim("name").asString();
        String role = scope[0];

        Member memberSelf = memberRepository.findByEmail(email).get();
        Member updateMember = memberRepository.findById(id).get();
        if(!userExists){
            return new ResponseEntity("User with given ID not found", HttpStatus.BAD_REQUEST);
        }else if(memberSelf.getRole().equals("ADMIN") || updateMember.getId().equals(UUID.fromString(user_id))){
            Member memberToUpdate = memberRepository.findById(id).get();
            memberToUpdate.setName(member.getName());
            memberToUpdate.setLastname(member.getLastname());
            memberToUpdate.setPassword(member.getPassword());
            memberRepository.save(memberToUpdate);
            return new ResponseEntity(memberToUpdate, HttpStatus.OK);
        } else {
            return new ResponseEntity("You are not allowed to update this user", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity delete(UUID id) {
        if(memberRepository.existsById(id)) {
            memberRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    public Optional<Member> getByEmailAndPassword(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password);
    }
}
