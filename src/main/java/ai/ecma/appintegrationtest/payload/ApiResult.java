package ai.ecma.appintegrationtest.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiResult<T> {
    private Boolean success;
    private T data;
    private List<ErrorData> errors;


    public ApiResult() {
    }

    public ApiResult(Boolean success) {
        this.success = success;
    }

    public ApiResult(T data) {
        this.data = data;
        success = Boolean.TRUE;
    }

    public ApiResult(String userMsg, Integer errorCode) {
        this.success = false;
        this.errors = Collections.singletonList(new ErrorData(userMsg, errorCode));
    }

    public ApiResult(List<ErrorData> errors) {
        this.success = false;
        this.errors = errors;
    }

    public static <E> ApiResult<E> successResponse(E data) {
        return new ApiResult<>(data);
    }

    public static <E> ApiResult<E> successResponse() {
        return new ApiResult<>(true);
    }
}
