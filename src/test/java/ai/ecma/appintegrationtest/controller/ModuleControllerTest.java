package ai.ecma.appintegrationtest.controller;

import ai.ecma.appintegrationtest.AppIntegrationTestApplication;
import ai.ecma.appintegrationtest.config.TestPostgresqlConfig;
import ai.ecma.appintegrationtest.entity.Course;
import ai.ecma.appintegrationtest.entity.Module;
import ai.ecma.appintegrationtest.payload.ApiResult;
import ai.ecma.appintegrationtest.payload.ModuleDTO;
import ai.ecma.appintegrationtest.payload.SignInDTO;
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
class ModuleControllerTest {
    private static final String adminUsername = "admin";
    private static final String adminPassword = "admin123";
    private static final String user = "user";
    private static final String userPassword = "user123";


    @Autowired
    private WebApplicationContext context;

    //BOSHQA TEST SERVEICLAR UCHUN TOKEN KERAK BO'LGANDA SHU STATIC FIELDNI CHAQIRISHADI
    public static String adminToken;
    public static String userToken;

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

//BOSHLANG'ICH USERLARNI QO'SHISH
        ApiResult<Boolean> saveUsersInitial = TestUtil.saveUserInInitial(HttpStatus.OK);

        //LOGIN QILYAPAMAN
        ApiResult<SignInDTO> signInDTOApiResult = TestUtil.signIn(adminUsername, adminPassword, HttpStatus.OK);

        //LOGIN DAN KELGAN OBJECTNI VAIDATSIYADAN O'TKAZDIK
        TestUtil.validateSuccessApiResponseWithNotNullData(signInDTOApiResult);

        //RESULTDAN TOKENNI OLYAPAMAN
        adminToken = signInDTOApiResult.getData().getToken();

        //LOGIN QILYAPAMAN
        ApiResult<SignInDTO> signInDTOApiResultUser = TestUtil.signIn(user, userPassword, HttpStatus.OK);

        //LOGIN DAN KELGAN OBJECTNI VAIDATSIYADAN O'TKAZDIK
        TestUtil.validateSuccessApiResponseWithNotNullData(signInDTOApiResultUser);

        Course course = new Course(1,"bla");
        ApiResult<String> result =
                TestUtil.addCourse(course, adminToken, HttpStatus.OK);
        TestUtil.validateSuccessApiResponse(result);

        //RESULTDAN TOKENNI OLYAPAMAN
        userToken = signInDTOApiResultUser.getData().getToken();
    }

    @Test
    @Order(value = 10)
    void addModuleSuccessCase() {
        ModuleDTO moduleDTO=new ModuleDTO(1,"java OOP",1,3,320.000);
        ApiResult<String> result =
                TestUtil.addModule(moduleDTO, adminToken, HttpStatus.OK);
        TestUtil.validateSuccessApiResponse(result);
    }

    @Test
    @Order(value = 20)
    void addModuleFailCase() {
        ModuleDTO moduleDTO=new ModuleDTO(1,"java OOP",1,3,320.000);
        ApiResult<String> result =
                TestUtil.addModule(moduleDTO, adminToken, HttpStatus.CONFLICT);
        TestUtil.validateFailApiResponse(result);
    }

    @Test
    @Order(value = 30)
    void addCourseFailCasePermissionDenied() {
        ModuleDTO moduleDTO=new ModuleDTO(3,"java advenced",1,3,320.000);
        ApiResult<String> result =
                TestUtil.addModule(moduleDTO, userToken, HttpStatus.FORBIDDEN);
        TestUtil.validateFailApiResponse(result);
    }


    @Test
    @Order(value = 50)
    void editCourseSuccessCase() {
        ModuleDTO moduleDTO=new ModuleDTO(1,"java Basic",1,3,220.000);
        ApiResult<String> result =
                TestUtil.editModule(moduleDTO, adminToken, HttpStatus.OK);
        TestUtil.validateSuccessApiResponse(result);
    }

    // Berilgan id lik course topilmadi
    @Test
    @Order(value = 60)
    void editModuleFailCase() {
        ModuleDTO moduleDTO=new ModuleDTO(122,"java Basic",1,3,220.000);
        ApiResult<String> result =
                TestUtil.editModule(moduleDTO, adminToken, HttpStatus.NOT_FOUND);
        TestUtil.validateFailApiResponse(result);
        Assertions.assertThat(result.getErrors().get(0).getErrorCode()).isEqualTo(404);
    }

    @Test
    @Order(value = 70)
    void editModuleNotPermissionCase() {
        ModuleDTO moduleDTO=new ModuleDTO(1,"python Basic",1,3,220.000);
        ApiResult<String> result =
                TestUtil.editModule(moduleDTO, userToken, HttpStatus.FORBIDDEN);
        TestUtil.validateFailApiResponse(result);
    }


    @Test
    @Order(value = 75)
    void getModulesSuccessCase() {
        ApiResult<?> apiResult = TestUtil.getModules(HttpStatus.OK);
        TestUtil.validateSuccessApiResponse(apiResult);
    }


    @Test
    @Order(value = 76)
    void getOneModuleSuccessCase() {
        ApiResult<?> apiResult = TestUtil.getOneModule(1, HttpStatus.OK);
        TestUtil.validateSuccessApiResponse(apiResult);
    }

    @Test
    @Order(value = 77)
    void getOneModuleFailCase() {
        ApiResult<?> apiResult = TestUtil.getOneModule(12, HttpStatus.NOT_FOUND);
        TestUtil.validateFailApiResponse(apiResult);
        Assertions.assertThat(apiResult.getErrors().get(0).getErrorCode()).isEqualTo(404);
    }



    @Test
    @Order(value = 80)
    void deleteModuleSuccessCase() {
        ApiResult<String> result =
                TestUtil.deleteModule(1, adminToken, HttpStatus.OK);
        TestUtil.validateSuccessApiResponse(result);
    }

    //Berilgan ID topilmaydi
    @Test
    @Order(value = 90)
    void deleteModuleFailCase() {
        ApiResult<String> result =
                TestUtil.deleteModule(333, adminToken, HttpStatus.NOT_FOUND);
        TestUtil.validateFailApiResponse(result);
        Assertions.assertThat(result.getErrors().get(0).getErrorCode()).isEqualTo(404);
    }

    @Test
    @Order(value = 100)
    void deleteCourseNotPermissionCase() {
        ApiResult<String> result =
                TestUtil.deleteModule(1, userToken, HttpStatus.FORBIDDEN);
        TestUtil.validateFailApiResponse(result);
    }


}

