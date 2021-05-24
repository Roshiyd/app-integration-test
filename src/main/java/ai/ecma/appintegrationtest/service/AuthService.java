package ai.ecma.appintegrationtest.service;

import ai.ecma.appintegrationtest.entity.User;
import ai.ecma.appintegrationtest.payload.RestException;
import ai.ecma.appintegrationtest.payload.SignInDTO;
import ai.ecma.appintegrationtest.payload.SignUpDTO;
import ai.ecma.appintegrationtest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Optional<User> signUp(SignUpDTO signUpDTO) {
        if (userRepository.existsByUsername(signUpDTO.getUsername()))
            throw new RestException("Bunday user mavjud", 409, HttpStatus.CONFLICT);
        userRepository.save(new User(
                signUpDTO.getUsername(),
                passwordEncoder.encode(signUpDTO.getPassword()),
                signUpDTO.getFirstName(),
                signUpDTO.getLastName()
        ));
        return Optional.of(new User(signUpDTO.getUsername()));
    }

    public SignInDTO signIn(SignInDTO signInDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInDTO.getUsername(),
                signInDTO.getPassword()
        ));
        return new SignInDTO("tokencha");
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s).orElseThrow(() -> new RestException("User topilmadi", 401, HttpStatus.UNAUTHORIZED));
    }
}
