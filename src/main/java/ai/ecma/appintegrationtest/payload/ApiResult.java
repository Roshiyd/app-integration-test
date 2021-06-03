package ai.ecma.appintegrationtest.payload;

import ai.ecma.appintegrationtest.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {
    private Boolean success;//TRUE, EXCEPTIONDAGI FALSE
    private String message;//SUCCESS TRUE BO'LGANDA QAYATDI XOLOS
    private T data;//SUCCESS TRUE BO'LGANDA QAYATDI XOLOS
    private List<ErrorData> errors;//SUCCESS FALSE BO'LGANDA QAYATDI XOLOS


    public ApiResult() {
    }

    //RESPONSE WITH BOOLEAN ONLY SUCCESS
    private ApiResult(Boolean success) {
        this.success = success;
    }


    //SUCCESS RESPONSE WITH DATA
    public ApiResult(T data, Boolean success) {
        this.data = data;
        this.success = Boolean.TRUE;
    }

    //SUCCESS RESPONSE WITH MESSAGE
    public ApiResult(String message) {
        this.message = message;
        this.success = Boolean.TRUE;
    }

    //ERROR RESPONSE WITH MESSAGE AND ERROR CODE
    public ApiResult(String errorMsg, Integer errorCode) {
        this.success = false;
        this.errors = Collections.singletonList(new ErrorData(errorMsg, errorCode));
    }


    //ERROR RESPONSE WITH ERROR LIST
    public ApiResult(List<ErrorData> errors) {
        this.success = false;
        this.errors = errors;
    }

    public static <E> ApiResult<E> successResponse(E data) {
        return new ApiResult<>(data, true);
    }


    public static ApiResult<Boolean> successResponse() {
        return new ApiResult<>(true);
    }

     public static ApiResult<String> successResponse(String message){
        return new ApiResult<>(message);
     }
}
