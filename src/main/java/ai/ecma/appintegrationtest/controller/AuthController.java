package ai.ecma.appintegrationtest.controller;

import ai.ecma.appintegrationtest.entity.User;
import ai.ecma.appintegrationtest.payload.ApiResult;
import ai.ecma.appintegrationtest.payload.RestException;
import ai.ecma.appintegrationtest.payload.SignInDTO;
import ai.ecma.appintegrationtest.payload.SignUpDTO;
import ai.ecma.appintegrationtest.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(path = AuthController.CONTROLLER_URI)
public class AuthController {

    public static final String CONTROLLER_URI = "/auth";
    public static final String SIGN_UP = "/sign_up";
    public static final String SIGN_IN = "/sign_in";

    @Autowired
    AuthService authService;


    @PostMapping(value = SIGN_UP)
    public ApiResult<Boolean> signUp(@Valid @RequestBody SignUpDTO signUpDTO) {
        Optional<User> optionalUser = authService.signUp(signUpDTO);

        optionalUser.orElseThrow(() -> new RestException("error", 409, HttpStatus.CONFLICT));
        return ApiResult.successResponse();
    }

    @PostMapping(value = SIGN_IN)
    public ApiResult<SignInDTO> signIn(@Valid @RequestBody SignInDTO signInDTO) {
//        return ApiResult.successResponse(new SignInDTO("tokencha"));
        return ApiResult.successResponse(authService.signIn(signInDTO));
    }

}
