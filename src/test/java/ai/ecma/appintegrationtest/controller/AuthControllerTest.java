package ai.ecma.appintegrationtest.controller;

import ai.ecma.appintegrationtest.AppIntegrationTestApplication;
import ai.ecma.appintegrationtest.config.TestPostgresqlConfig;
import ai.ecma.appintegrationtest.entity.User;
import ai.ecma.appintegrationtest.payload.ApiResult;
import ai.ecma.appintegrationtest.payload.SignInDTO;
import ai.ecma.appintegrationtest.payload.SignUpDTO;
import ai.ecma.appintegrationtest.repository.UserRepository;
import ai.ecma.appintegrationtest.utils.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@AutoConfigureMockMvc
@Import({TestPostgresqlConfig.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.NONE, replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {AppIntegrationTestApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest {
    private static final String email = "username" + TestUtil.RANDOM.nextInt(10000) + "username";
    private static final String password = "password";

    @Autowired
    private WebApplicationContext context;
    //    @MockBean
//    private AuthService authService;
//    private ArgumentCaptor<SignUpDTO> userArgumentCaptor = ArgumentCaptor.forClass(SignUpDTO.class);
    private static String token;
    @Autowired
    UserRepository userRepository;


    @BeforeAll
    public void initialiseRestAssuredMockMvcWebApplicationContext() {
        MockitoAnnotations.initMocks(this);
        RestAssuredMockMvc.standaloneSetup(MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        SignUpDTO signUpDTO = new SignUpDTO(email, password);

        ApiResult<SignUpDTO> signUpApiResult = TestUtil.registerUser(signUpDTO, HttpStatus.OK);
        TestUtil.validateSuccessApiResponse(signUpApiResult);
        Optional<User> aa = userRepository.findByUsername("aa");
        System.out.println(aa.isEmpty());
//
//        //VERIFY/ACTIVATE EMAIL/ACCOUNT
//        Mockito.verify(authService, Mockito.times(1))
//                .signUp(userArgumentCaptor.capture());
//        String username = userArgumentCaptor.getValue().getUsername();
        ApiResult<SignInDTO> signInDTOApiResult = TestUtil.signIn(signUpDTO.getUsername(), signUpDTO.getPassword(), HttpStatus.OK);
        token = signInDTOApiResult.getData().getToken();
//
//        ApiResult<UserLoginDTO> signUpApiResult = TestUtil.registerVerifyLogin(email, password, emailServiceImpl);
//        TestUtil.validateSuccessApiResponseWithNotNullData(signUpApiResult);
//        token = signUpApiResult.getData().getToken();
    }

    @Test
    @Order(value = 10)
    void signUpSuccessCase() {
        SignUpDTO signUpDTO = new SignUpDTO("siroj", "root123", "Sirojiddin", "Saidov");
        ApiResult<SignUpDTO> result =
                TestUtil.registerUser(signUpDTO, HttpStatus.OK);
        Assertions.assertThat(result.getSuccess()).isTrue();
        Assertions.assertThat(result.getErrors()).isNull();
    }

    @Test
    @Order(value = 20)
    void signUpFailCase() {
        SignUpDTO signUpDTO = new SignUpDTO("siroj", "root123", "Sirojiddin", "Saidov");
        ApiResult<SignUpDTO> result =
                TestUtil.registerUser(signUpDTO, HttpStatus.CONFLICT);
        Assertions.assertThat(result.getSuccess()).isFalse();
        Assertions.assertThat(result.getErrors()).isNotNull();
        Assertions.assertThat(result.getErrors()).isNotEmpty();
        Assertions.assertThat(result.getErrors().get(0).getError_code()).isGreaterThan(399);
        Assertions.assertThat(result.getData()).isNull();
    }

    @Test
    @Order(value = 30)
    void signInSuccessCase() {
        TestUtil.signIn("siroj", "root123", HttpStatus.OK);
    }
}

