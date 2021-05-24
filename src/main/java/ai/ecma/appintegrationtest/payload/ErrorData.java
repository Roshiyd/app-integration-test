package ai.ecma.appintegrationtest.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorData {
    private String user_msg;
    private Integer error_code;
}
