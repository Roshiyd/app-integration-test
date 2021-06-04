
package ai.ecma.appintegrationtest.entity;
import ai.ecma.appintegrationtest.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
/*@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name","course_id"})})*/
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;

    private Integer orders;

    @Column(nullable = false)
    private Double price;
}
