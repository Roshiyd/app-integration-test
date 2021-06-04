package ai.ecma.appintegrationtest.controller;

import ai.ecma.appintegrationtest.AppIntegrationTestApplication;
import ai.ecma.appintegrationtest.config.TestPostgresqlConfig;
import ai.ecma.appintegrationtest.entity.Course;
import ai.ecma.appintegrationtest.payload.ApiResult;
import ai.ecma.appintegrationtest.payload.LessonDTO;
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
class LessonControllerTest {
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
        ApiResult<String> resultCourse =
                TestUtil.addCourse(course, adminToken, HttpStatus.OK);
        TestUtil.validateSuccessApiResponse(resultCourse);

        ModuleDTO moduleDTO=new ModuleDTO(1,"java OOP",1,3,320.000);
        ApiResult<String> resultModule =
                TestUtil.addModule(moduleDTO, adminToken, HttpStatus.OK);
        TestUtil.validateSuccessApiResponse(resultModule);

        //RESULTDAN TOKENNI OLYAPAMAN
        userToken = signInDTOApiResultUser.getData().getToken();
    }

    @Test
    @Order(value = 10)
    void addLessonSuccessCase() {
        LessonDTO lessonDTO=new LessonDTO(1,"lesson1",1);
        ApiResult<String> result =
                TestUtil.addLesson(lessonDTO, adminToken, HttpStatus.OK);
        TestUtil.validateSuccessApiResponse(result);
    }

    @Test
    @Order(value = 20)
    void addLessonFailCase() {
        LessonDTO lessonDTO=new LessonDTO(1,"lesson1",1);
        ApiResult<String> result =
                TestUtil.addLesson(lessonDTO, adminToken, HttpStatus.CONFLICT);
        TestUtil.validateFailApiResponse(result);
    }

    @Test
    @Order(value = 30)
    void addLessonFailCasePermissionDenied() {
        LessonDTO lessonDTO=new LessonDTO(2,"lesson2",1);
        ApiResult<String> result =
                TestUtil.addLesson(lessonDTO, userToken, HttpStatus.FORBIDDEN);
        TestUtil.validateFailApiResponse(result);
    }




    @Test
    @Order(value = 50)
    void editLessonSuccessCase() {
        LessonDTO lessonDTO=new LessonDTO(1,"lesson11",1);
        ApiResult<String> result =
                TestUtil.editLesson(lessonDTO, adminToken, HttpStatus.OK);
        TestUtil.validateSuccessApiResponse(result);
    }

    // Berilgan id lik course topilmadi
    @Test
    @Order(value = 60)
    void editLessonFailCase() {
        LessonDTO lessonDTO=new LessonDTO(111,"lesson11",1);
        ApiResult<String> result =
                TestUtil.editLesson(lessonDTO, adminToken, HttpStatus.NOT_FOUND);
        TestUtil.validateFailApiResponse(result);
        Assertions.assertThat(result.getErrors().get(0).getErrorCode()).isEqualTo(404);
    }

    @Test
    @Order(value = 70)
    void editLessonNotPermissionCase() {
        LessonDTO lessonDTO=new LessonDTO(1,"lesson11",1);
        ApiResult<String> result =
                TestUtil.editLesson(lessonDTO, userToken, HttpStatus.FORBIDDEN);
        TestUtil.validateFailApiResponse(result);
    }


    @Test
    @Order(value = 75)
    void getLessonsSuccessCase() {
        ApiResult<?> apiResult = TestUtil.getLessons(HttpStatus.OK);
        TestUtil.validateSuccessApiResponse(apiResult);
    }
    @Test
    @Order(value = 76)
    void getOneLessonSuccessCase() {
        ApiResult<?> apiResult = TestUtil.getOneLesson(1, HttpStatus.OK);
        TestUtil.validateSuccessApiResponse(apiResult);
    }

    @Test
    @Order(value = 77)
    void getOneLessonFailCase() {
        ApiResult<?> apiResult = TestUtil.getOneLesson(122, HttpStatus.NOT_FOUND);
        TestUtil.validateFailApiResponse(apiResult);
        Assertions.assertThat(apiResult.getErrors().get(0).getErrorCode()).isEqualTo(404);
    }



    @Test
    @Order(value = 80)
    void deleteLessonSuccessCase() {
        ApiResult<String> result =
                TestUtil.deleteLesson(1, adminToken, HttpStatus.OK);
        TestUtil.validateSuccessApiResponse(result);
    }

    //Berilgan ID topilmaydi
    @Test
    @Order(value = 90)
    void deleteLessonFailCase() {
        ApiResult<String> result =
                TestUtil.deleteLesson(333, adminToken, HttpStatus.NOT_FOUND);
        TestUtil.validateFailApiResponse(result);
        Assertions.assertThat(result.getErrors().get(0).getErrorCode()).isEqualTo(404);
    }

    @Test
    @Order(value = 100)
    void deleteCourseNotPermissionCase() {
        ApiResult<String> result =
                TestUtil.deleteLesson(1, userToken, HttpStatus.FORBIDDEN);
        TestUtil.validateFailApiResponse(result);
    }



}

