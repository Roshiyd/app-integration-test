package ai.ecma.appintegrationtest.repository;

import ai.ecma.appintegrationtest.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson,Integer> {

    boolean existsByName( String name);

}
