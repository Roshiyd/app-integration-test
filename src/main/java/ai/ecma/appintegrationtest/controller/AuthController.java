package ai.ecma.appintegrationtest.controller;

import ai.ecma.appintegrationtest.component.DataLoader;
import ai.ecma.appintegrationtest.entity.User;
import ai.ecma.appintegrationtest.payload.ApiResult;
import ai.ecma.appintegrationtest.payload.RestException;
import ai.ecma.appintegrationtest.payload.SignInDTO;
import ai.ecma.appintegrationtest.payload.SignUpDTO;
import ai.ecma.appintegrationtest.service.AuthService;
import ai.ecma.appintegrationtest.utils.RestConstants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = AuthController.CONTROLLER_URI)
public class AuthController {

    public static final String CONTROLLER_URI = RestConstants.API_V_1 + "/auth";
    public static final String SIGN_UP = "/sign-up";
    public static final String SIGN_IN = "/sign-in";

    final
    AuthService authService;
    final DataLoader dataLoader;

    public AuthController(AuthService authService, DataLoader dataLoader) {
        this.authService = authService;
        this.dataLoader = dataLoader;
    }

    @PostMapping(value = SIGN_UP)
    public ApiResult<Boolean> signUp(@Valid @RequestBody SignUpDTO signUpDTO) {
        Optional<User> optionalUser = authService.signUp(signUpDTO);
        optionalUser.orElseThrow(() -> new RestException("error", HttpStatus.CONFLICT));
        return ApiResult.successResponse();
    }

    @PostMapping(value = SIGN_IN)
    public ApiResult<SignInDTO> signIn(@Valid @RequestBody SignInDTO signInDTO) {

        return ApiResult.successResponse(authService.signIn(signInDTO));
    }

    @GetMapping("/first")
    public HttpEntity<?> getFirst() {
        return ResponseEntity.status(401).body(new SignInDTO("bekzod", "root123"));
    }


    @GetMapping("/second")
    public HttpEntity<?> getSecond() {
        List<SignInDTO> signInDTOList = new ArrayList<>(Arrays.asList(
                new SignInDTO("bekzod", "root123"),
                new SignInDTO("bekzod2", "root123"),
                new SignInDTO("bekzod3", "root123")
        ));
        return ResponseEntity.status(200).body(signInDTOList);
    }

    @GetMapping("/third")
    public HttpEntity<?> getThird() {
        return ResponseEntity.status(200).body("Qalay okam");
    }

    @GetMapping("/fourth")
    public ApiResult<Boolean> getFourth() {
        dataLoader.saveUsers();
        return ApiResult.successResponse();
    }


}
