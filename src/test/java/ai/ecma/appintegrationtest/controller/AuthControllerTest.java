package ai.ecma.appintegrationtest.controller;

import ai.ecma.appintegrationtest.AppIntegrationTestApplication;
import ai.ecma.appintegrationtest.config.TestPostgresqlConfig;
import ai.ecma.appintegrationtest.payload.ApiResult;
import ai.ecma.appintegrationtest.payload.SignInDTO;
import ai.ecma.appintegrationtest.payload.SignUpDTO;
import ai.ecma.appintegrationtest.utils.TestUtil;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@AutoConfigureMockMvc//MOCK NVC NI AUTO SOZLASH UCHUN, ONLY TESTING
@Import({TestPostgresqlConfig.class})
@ExtendWith(SpringExtension.class)//ONLY FOR TESTING
@AutoConfigureTestDatabase(connection =
        EmbeddedDatabaseConnection.NONE,
        replace = AutoConfigureTestDatabase.Replace.NONE)//DB NI AUTO CONFIG QILMA DEDIM
@TestInstance(TestInstance.Lifecycle.PER_CLASS)//TESTNING HAYOTI QANDAY KECHISHI
@SpringBootTest(classes = {AppIntegrationTestApplication.class})//QAYSI REAL LOYIHANI ICHINI TEST QILISHI KERAKLIGI
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)//METHODARNI TARTIB BILAN ISHALSHINI TA'MINLASH UCHUN
@DirtiesContext//CONTEXTDAN OBJECTLARNI TOZALAMA
@ActiveProfiles("test")//QAYSI PROPERTIESDAN O'QISHINI AYTISH KERAK
class AuthControllerTest {
    private static final String username = "username" + TestUtil.RANDOM.nextInt(10000) + "username";
    private static final String password = "root123";
    private static final String firstName = "Ketmon";
    private static final String lastName = "Teshayev";


    @Autowired
    private WebApplicationContext context;

    //BOSHQA TEST SERVEICLAR UCHUN TOKEN KERAK BO'LGANDA SHU STATIC FIELDNI CHAQIRISHADI
    public static String token;

//    @Autowired
//    UserRepository userRepository;
//

    @BeforeAll
    public void initialiseRestAssuredMockMvcWebApplicationContext() {
        //CONFIG
        MockitoAnnotations.openMocks(this);
        RestAssuredMockMvc.standaloneSetup(MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()));

        TestUtil.saveUserInInitial(HttpStatus.OK);
        //MY
        SignUpDTO signUpDTO = new SignUpDTO(username,firstName,lastName,password,password);
        //API GA SO'ROV YUBORDIM
        ApiResult<Boolean> signUpApiResult = TestUtil.registerUser(signUpDTO, HttpStatus.OK);

        //REGISTER BO'LGANINI TEKSHIRDIM
        TestUtil.validateSuccessApiResponse(signUpApiResult);

        //LOGIN QILYAPAMAN
        ApiResult<SignInDTO> signInDTOApiResult = TestUtil.signIn(username, password, HttpStatus.OK);

        //LOGIN DAN KELGAN OBJECTNI VAIDATSIYADAN O'TKAZDIK
        TestUtil.validateSuccessApiResponseWithNotNullData(signInDTOApiResult);

        //RESULTDAN TOKENNI OLYAPAMAN
        /*token = signInDTOApiResult.getData().getToken();*/
    }

    @Test
    @Order(value = 10)
    void signUpSuccessCase() {
        SignUpDTO signUpDTO = new SignUpDTO("username","Sirojiddin", "Sirojiddin", "root123", "root123");
        ApiResult<Boolean> result =
                TestUtil.registerUser(signUpDTO, HttpStatus.OK);
        TestUtil.validateSuccessApiResponse(result);
    }

    //Password va PrePassword match bo'lmasligi
    @Test
    @Order(value = 20)
    void signUpFailCase() {
        SignUpDTO signUpDTO = new SignUpDTO("username","siroj", "Sirojiddin", "root123", "root1234");
        ApiResult<Boolean> result =
                TestUtil.registerUser(signUpDTO, HttpStatus.CONFLICT);
        TestUtil.validateFailApiResponse(result);
//        Assertions.assertThat(result.getErrors().get(0).getErrorCode()).isGreaterThan(399).isLessThan(500);
        Assertions.assertThat(result.getErrors().get(0).getErrorCode()).isEqualTo(409);
    }

    //Password va PrePassword match bo'lmasligi
    @Test
    @Order(value = 20)
    void signUpUsernameExistFailCase() {
        SignUpDTO signUpDTO = new SignUpDTO("username","siroj", "Sirojiddin", "root123", "root123");
        ApiResult<Boolean> result =
                TestUtil.registerUser(signUpDTO, HttpStatus.CONFLICT);
        TestUtil.validateFailApiResponse(result);
//        Assertions.assertThat(result.getErrors().get(0).getErrorCode()).isGreaterThan(399).isLessThan(500);
        Assertions.assertThat(result.getErrors().get(0).getErrorCode()).isEqualTo(409);
    }

    @Test
    @Order(value = 30)
    void signInSuccessCase() {
        ApiResult<SignInDTO> result = TestUtil.signIn("username", "root123", HttpStatus.OK);
        TestUtil.validateSuccessApiResponseWithNotNullData(result);
    }

    @Test
    @Order(value = 40)
    void signInFailCase() {
        ApiResult<SignInDTO> result = TestUtil.signIn("siroj", "root1234", HttpStatus.UNAUTHORIZED);
        TestUtil.validateFailApiResponse(result);
        Assertions.assertThat(result.getErrors().get(0).getErrorCode()).isEqualTo(401);
    }
}

