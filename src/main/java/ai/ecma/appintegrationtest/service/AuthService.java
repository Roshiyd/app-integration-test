package ai.ecma.appintegrationtest.service;

import ai.ecma.appintegrationtest.entity.User;
import ai.ecma.appintegrationtest.payload.RestException;
import ai.ecma.appintegrationtest.payload.SignInDTO;
import ai.ecma.appintegrationtest.payload.SignUpDTO;
import ai.ecma.appintegrationtest.repository.RoleRepository;
import ai.ecma.appintegrationtest.repository.UserRepository;
import ai.ecma.appintegrationtest.utils.RestConstants;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {
    final
    UserRepository userRepository;
    final
    AuthenticationManager authenticationManager;
    final
    PasswordEncoder passwordEncoder;
    final RoleRepository roleRepository;
    final JwtProvider jwtProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RoleRepository roleRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.jwtProvider = jwtProvider;
    }

    public Optional<User> signUp(SignUpDTO signUpDTO) {
        if (!signUpDTO.getPassword().equals(signUpDTO.getPrePassword())){
            throw new RestException("Parol mos emas",HttpStatus.CONFLICT);
        }
        if (userRepository.existsByUsername(signUpDTO.getUsername()))
            throw new RestException("Bunday user mavjud", HttpStatus.CONFLICT);

        User savedUser = userRepository.save(new User(
                signUpDTO.getUsername(),
                passwordEncoder.encode(signUpDTO.getPassword()),
                signUpDTO.getFirstName(),
                signUpDTO.getLastName(),
                roleRepository.findByName(RestConstants.USER).orElseThrow(() ->new RestException("Bunday role mavjud emas",HttpStatus.NOT_FOUND))
        ));
        return Optional.of(savedUser);
    }

    public SignInDTO signIn(SignInDTO signInDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    signInDTO.getUsername(),
                    signInDTO.getPassword()
            ));
            return new SignInDTO(jwtProvider.generateToken(signInDTO.getUsername()));
        } catch (BadCredentialsException e) {
            throw new RestException("Parol yoki login xato", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s).orElseThrow(() -> new RestException("User topilmadi", HttpStatus.UNAUTHORIZED));
    }
}
