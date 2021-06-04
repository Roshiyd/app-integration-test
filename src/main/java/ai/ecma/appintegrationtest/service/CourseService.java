package ai.ecma.appintegrationtest.service;

import ai.ecma.appintegrationtest.entity.Course;
import ai.ecma.appintegrationtest.payload.ApiResult;
import ai.ecma.appintegrationtest.payload.RestException;
import ai.ecma.appintegrationtest.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    public Optional<Course> addCourse(Course course) {

        boolean existsByName = courseRepository.existsByName(course.getName());
        if (existsByName) {
            throw new RestException("Bazada bunda name mavjud", HttpStatus.CONFLICT);
        }

        Course savedCourse = courseRepository.save(course);
        return Optional.of(savedCourse);
    }


    public Optional<Course> editCourse(Course course) {

        Optional<Course> optionalCourse = courseRepository.findById(course.getId());

        if (!optionalCourse.isPresent()) {
            throw new RestException("Bunday course topilmadi", HttpStatus.NOT_FOUND);
        }

        Course current = optionalCourse.get();
        current.setName(course.getName());
        current.setDescription(course.getDescription());
        current.setUrl(course.getUrl());
        return Optional.of(courseRepository.save(current));

    }

    public Course getCourse(Integer id) {

        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (!optionalCourse.isPresent()) {
            throw new RestException("Bunday Course topilmadi", HttpStatus.NOT_FOUND);
        }

        return optionalCourse.get();
    }

    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    public ApiResult<Boolean> deleteCourse(Integer id) {

        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (!optionalCourse.isPresent()) {
            throw new RestException("Bunday Course topilamdi", HttpStatus.NOT_FOUND);
        }
        courseRepository.deleteById(id);
        return ApiResult.successResponse(true);

    }
}
