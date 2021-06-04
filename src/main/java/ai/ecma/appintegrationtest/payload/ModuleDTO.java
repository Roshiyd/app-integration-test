package ai.ecma.appintegrationtest.payload;

import ai.ecma.appintegrationtest.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleDTO {
    @NotNull(message = "ID kiritilishi shart")
    private Integer id;
    @NotBlank(message = "Name kiritilishi shart")
    private String name;

    private String description;

    private Integer courseId;

    private Integer order;

    @NotNull(message = "Price kiritilishi shart")
    private Double price;


    public ModuleDTO(Integer id, String name, Integer courseId, Integer order, Double price) {
        this.id = id;
        this.name = name;
        this.courseId = courseId;
        this.order = order;
        this.price = price;
    }
}
