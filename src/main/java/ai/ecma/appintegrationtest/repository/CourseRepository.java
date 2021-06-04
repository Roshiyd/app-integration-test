package ai.ecma.appintegrationtest.repository;

import ai.ecma.appintegrationtest.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotBlank;

public interface CourseRepository extends JpaRepository<Course,Integer> {

    boolean existsByName( String name);
    boolean existsByUrl( String url);
}
