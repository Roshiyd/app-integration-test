package ai.ecma.appintegrationtest.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpDTO {

    @NotBlank(message = "Iltimos username bo'sh bo'lmasin")
    private String username;

    @NotBlank(message = "Iltimos ismingiz bo'sh bo'lmasin")
    private String firstName;

    @NotBlank(message = "Iltimos familyangiz bo'sh bo'lmasin")
    private String lastName;

    @NotBlank(message = "password filed is empty!!!")
    private String password;

    @NotBlank(message = "prePassword filed is empty!!!")
    private String prePassword;

    public SignUpDTO(String firstName, String lastName, String password, String prePassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.prePassword = prePassword;
    }
}
