package ai.ecma.appintegrationtest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Locale;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,unique = true)
    @NotBlank(message = "name kiritishi shart")
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false,unique = true)
//    @NotBlank(message = "url bo'lishi shart")
    private String url;

    @PrePersist
    @PreUpdate
    private void generateUrl(){
        if (name != null){
            this.url = name.replaceAll( " ","-")
                    .toLowerCase();
        }
    }

    public Course(String name) {
        this.name = name;
    }

    public Course(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
