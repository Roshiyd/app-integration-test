package ai.ecma.appintegrationtest.utils;


import ai.ecma.appintegrationtest.controller.AuthController;
import ai.ecma.appintegrationtest.controller.CourseController;
import ai.ecma.appintegrationtest.entity.Course;
import ai.ecma.appintegrationtest.payload.ApiResult;
import ai.ecma.appintegrationtest.payload.SignInDTO;
import ai.ecma.appintegrationtest.payload.SignUpDTO;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.experimental.UtilityClass;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Random;

@UtilityClass
public class TestUtil {

    public final String AUTH_CONTROLLER_PATH = AuthController.CONTROLLER_URI;
    public final String COURSE_CONTROLLER_PATH = CourseController.COURSE_URL;
    public final Random RANDOM = new Random();
//    private final ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
//    private final ArgumentCaptor<String> hostArgumentCaptor = ArgumentCaptor.forClass(String.class);
//    private final ArgumentCaptor<String> tokenArgumentCaptor = ArgumentCaptor.forClass(String.class);

    public ApiResult<Boolean> registerUser(SignUpDTO signUpDTO, HttpStatus httpStatus) {
        return TestUtil.postRequest(
                signUpDTO,
                TestUtil.AUTH_CONTROLLER_PATH + AuthController.SIGN_UP,
                new TypeRef<ApiResult<Boolean>>() {
                }, null, httpStatus);
    }

    public ApiResult<SignInDTO> signIn(String email, String password, HttpStatus httpStatus) {
        SignInDTO signInDTO = new SignInDTO(email, password);
        return TestUtil.postRequest(signInDTO,
                AuthController.CONTROLLER_URI + AuthController.SIGN_IN,
                new TypeRef<>() {
                }, null, httpStatus);
    }


    public ApiResult<Boolean> saveUserInInitial(HttpStatus httpStatus) {
        return TestUtil.getRequest(
                AuthController.CONTROLLER_URI + "/fourth",
                new TypeRef<>() {
                }, null, httpStatus);
    }

//    public void mockSecurityContext(TokenProvider tokenProvider, String token) {
//        //Mock SecurityContext, temporary solution, need clarify why JWTFilter is not calling in tests
//        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//
//        Authentication authentication = tokenProvider.getAuthentication(token);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//    }

//    public ApiResult<JWTToken> activateUser(String activationToken, String password) {
//
//        ResetPasswordDTO dto = new ResetPasswordDTO();
//        dto.setKey(activationToken);
//        dto.setPassword(password);
//
//        return TestUtil.postRequest(dto, TestUtil.AUTH_CONTROLLER_PATH + AuthController.ACTIVATE, new TypeRef<ApiResult<JWTToken>>() {
//        }, null, HttpStatus.OK);


        /*RestAssuredMockMvc.standaloneSetup(pagesController);
        RestAssuredMockMvc
                .given()
                .param("token", URLDecoder.decode(verificationToken, StandardCharsets.UTF_8))
                .log().all()
                .when()
                .get(PagesController.VERIFY)
                .then()
                .log().all()
                .statusCode(200);*/
//    }


//    public ApiResult<UserLoginDTO> registerVerifyLogin(String email, String password, MailService mailService) {
//        //SIGN UP
//        ApiResult<UserLoginDTO> apiResult = registerUser(email, password, "Firstname", "Lastname", HttpStatus.OK);
//        validateSuccessApiResponse(apiResult);
////        Assertions.assertThat(apiResult.getData().getMessage()).isEqualTo("Please check your email to verify account");
//
//        //VERIFY/ACTIVATE EMAIL/ACCOUNT
//        Mockito.verify(mailService, Mockito.times(1))
//                .sendActivationEmail(userArgumentCaptor.capture(), tokenArgumentCaptor.capture());
//        String activationToken = userArgumentCaptor.getValue().getActivationKey();
//        log.debug("Activation token:{}", activationToken);
//        ApiResult<JWTToken> activationResult = activateUser(activationToken, password);
//        validateSuccessApiResponse(activationResult);
//
//        //SIGN IN/LOGIN
//        return TestUtil.signIn(email, password);
//    }

    public <T> List<T> getAndValidateNonEmptyList(ApiResult<List<T>> apiResult) {
        validateSuccessApiResponseWithNotNullData(apiResult);
        Assertions.assertThat(apiResult.getData()).isNotEmpty();
        return apiResult.getData();
    }

    public <T> void validateSuccessApiResponse(ApiResult<T> apiResult) {
        Assertions.assertThat(apiResult.getSuccess()).isTrue();
        Assertions.assertThat(apiResult.getErrors()).isNull();
    }

    public <T> void validateFailApiResponse(ApiResult<T> apiResult) {
        Assertions.assertThat(apiResult.getSuccess()).isFalse();
        Assertions.assertThat(apiResult.getErrors()).isNotEmpty();
        Assertions.assertThat(apiResult.getErrors()).isNotNull();
        Assertions.assertThat(apiResult.getData()).isNull();
    }

    public <T> void validateSuccessApiResponseWithNotNullData(ApiResult<T> apiResult) {
        Assertions.assertThat(apiResult.getSuccess()).isTrue();
        Assertions.assertThat(apiResult.getErrors()).isNull();
        Assertions.assertThat(apiResult.getData()).isNotNull();
    }

    public <T> T getRequest(String url, TypeRef<T> typeRef, String token, HttpStatus httpStatus) {
        return RestAssuredMockMvc
                .given()
                .header(RestConstants.AUTHENTICATION_HEADER, token == null ? "" : token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .log().all()
                .when()
                .get(url)
                .then()
                .log().all()
                .statusCode(httpStatus.value())
                .contentType(ContentType.JSON)
                .extract()
                .body()
                .as(typeRef);
    }

//    public <T> byte[] getRequestForPdf(String url, String token, HttpStatus httpStatus) {
//        return RestAssuredMockMvc
//                .given()
//                .header(RestConstants.AUTHENTICATION_HEADER, token == null ? "" : token)
//                .contentType(ContentType.JSON)
//                .accept(MediaType.APPLICATION_PDF_VALUE)
//                .log().all()
//                .when()
//                .get(url)
//                .then()
//                .log().all()
//                .statusCode(httpStatus.value())
//                .contentType(MediaType.APPLICATION_PDF_VALUE)
//                .extract()
//                .body()
//                .asByteArray();
//    }

    public <T> T postRequest(Object object,
                             String postUrl,
                             TypeRef<T> typeRef,
                             String token,
                             HttpStatus httpStatus) {
        token = "Bearer " + token;
        return RestAssuredMockMvc
                .given()
                .header(RestConstants.AUTHENTICATION_HEADER, token == null ? "" : token)
                .header(RestConstants.LANGUAGE_HEADER, "en")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(object)
                .log().all()
                .when()
                .post(postUrl)
                .then()
                .log().all()
                .statusCode(httpStatus.value())
                .contentType(ContentType.JSON)
                .extract()
                .body()
                .as(typeRef);
    }

    public static ApiResult<String> addCourse(Course course, String token, HttpStatus httpStatus) {
        return TestUtil.postRequest(
                course,
                TestUtil.COURSE_CONTROLLER_PATH,
                new TypeRef<ApiResult<String>>() {
                }, token, httpStatus);
    }

    public static ApiResult<String> editCourse(Course course, String token, HttpStatus httpStatus) {
        return TestUtil.putRequest(
                course,
                TestUtil.COURSE_CONTROLLER_PATH,
                new TypeRef<ApiResult<String>>() {
                }, token, httpStatus);
    }

//    public <T> T deleteRequest(String deleteUrl, TypeRef<T> typeRef, String token, HttpStatus httpStatus) {
//        return RestAssuredMockMvc
//                .given()
//                .header(RestConstants.AUTHENTICATION_HEADER, token == null ? "" : token)
//                .contentType(ContentType.JSON)
//                .accept(ContentType.JSON)
//                .log().all()
//                .when()
//                .delete(deleteUrl)
//                .then()
//                .log().all()
//                .statusCode(httpStatus.value())
//                .contentType(ContentType.JSON)
//                .extract()
//                .body()
//                .as(typeRef);
//    }

//    public <T> T postRequestWithLanguageHeader(Object object,
//                                               String postUrl,
//                                               TypeRef<T> typeRef,
//                                               String token,
//                                               HttpStatus httpStatus) {
//        return RestAssuredMockMvc
//                .given()
//                .header(RestConstants.AUTHENTICATION_HEADER, token == null ? "" : token)
//                .header(RestConstants.LANGUAGE_HEADER, "en")
//                .contentType(ContentType.JSON)
//                .accept(ContentType.JSON)
//                .body(object)
//                .log().all()
//                .when()
//                .post(postUrl)
//                .then()
//                .log().all()
//                .statusCode(httpStatus.value())
//                .contentType(ContentType.JSON)
//                .extract()
//                .body()
//                .as(typeRef);
//    }
//
    public <T> T putRequest(Object body, String putUrl, TypeRef<T> typeRef, String token, HttpStatus httpStatus) {
        return RestAssuredMockMvc
                .given()
                .header(RestConstants.AUTHENTICATION_HEADER, token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(body)
                .log().all()
                .when()
                .put(putUrl)
                .then()
                .log().all()
                .statusCode(httpStatus.value())
                .contentType(ContentType.JSON)
                .extract()
                .body()
                .as(typeRef);
    }

    public String withBearer(String token) {
        return "Bearer " + token;
    }

    /*public static Map<String, Integer> getStatData() {
        return getRequest(MonitoringController.MANAGEMENT + MonitoringController.AVG_BY_KEY, new TypeRef<>() {
        }, null, HttpStatus.OK);
    }*/
}
