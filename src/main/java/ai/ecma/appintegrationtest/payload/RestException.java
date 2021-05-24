package ai.ecma.appintegrationtest.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class RestException extends RuntimeException {

    private String user_msg;
    private Integer error_code;
    private HttpStatus status;

    public RestException(String user_msg, Integer error_code, HttpStatus status) {
        super(user_msg);
        this.user_msg = user_msg;
        this.error_code = error_code;
        this.status = status;
    }

}
