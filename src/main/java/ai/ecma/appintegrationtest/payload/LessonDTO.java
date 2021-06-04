package ai.ecma.appintegrationtest.payload;

import ai.ecma.appintegrationtest.entity.Module;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO {


    @NotNull(message = "ID kiritilishi shart")
    private Integer id;

    @NotBlank(message = "Name kiritilishi shart")
    private String name;

    private String description;

    private Integer moduleId;

    private Integer order;

    public LessonDTO(Integer id, String name, Integer moduleId) {
        this.id = id;
        this.name = name;
        this.moduleId = moduleId;
    }
}
