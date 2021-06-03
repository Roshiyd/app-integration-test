package ai.ecma.appintegrationtest.utils;

public interface RestConstants {
    String PACKAGE_CONTROLLERS_REST = "com.kpi.uz.faktura.controllers.rest";
    String PACKAGE_CONTROLLERS_VIEW = "com.kpi.uz.faktura.controllers.view";

    String LOCALHOST = "localhost";

    String API_V_1 = "/api/v1";
    String API_UTILS_V_1 = "/api/v1/utils";

    String LANGUAGE_HEADER = "language";
    String AUTHENTICATION_HEADER = "Authorization";
    String ADMIN = "ROLE_ADMIN";
    String EMPLOYEE = "ROLE_EMPLOYEE";
    String DEVELOPER = "ROLE_DEVELOPER";
    String USER = "ROLE_USER";
    String ANONYMOUS = "ROLE_ANONYMOUS";

    String GENERAL_ERROR_MESSAGE = "Something went wrong. Please, try again later";
    String TYPE_MISMATCH_ERROR_MESSAGE = "Passed argument type mismatch";
    String ACCEPT_HEADER_MEDIA_TYPES_ERROR_MESSAGE = "Accept media type is not supported, supported media types are %s";
    String FILE_TOO_LARGE_ERROR_MESSAGE = "File too large.";
    String USER_NOT_FOUND = "User not found";
    String NO_PERMISSION = "Access Denied. You don't have permission to access";

    /*REST API Error codes*/
    int INCORRECT_USERNAME_OR_PASSWORD = 3001;
    int EMAIL_OR_PHONE_EXIST = 3002;
    int EXPIRED = 3003;
    int ACCESS_DENIED = 3004;
    int NOT_FOUND = 3005;
    int INVALID = 3006;
    int REQUIRED = 3007;
    int SERVER_ERROR = 3008;
    int CONFLICT = 3009;
    int NO_ITEMS_FOUND = 3011;
    int CONFIRMATION = 3012;
    int USER_NOT_ACTIVE = 3013;
    int JWT_TOKEN_INVALID = 3014;
}
