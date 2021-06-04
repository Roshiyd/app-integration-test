package ai.ecma.appintegrationtest.controller;

import ai.ecma.appintegrationtest.aop.CheckPermission;
import ai.ecma.appintegrationtest.entity.Course;
import ai.ecma.appintegrationtest.payload.ApiResult;
import ai.ecma.appintegrationtest.payload.RestException;
import ai.ecma.appintegrationtest.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(CourseController.COURSE_URL)
public class CourseController {

    public static final String COURSE_URL = "/api/course";
//    public static final String ADD = "/add";
//    public static final String EDIT = "/edit";

    @Autowired
    CourseService courseService;

    @CheckPermission(permission = {"ADD_COURSE"})
    @PostMapping
    public ApiResult<String> addCourse(@Valid @RequestBody Course course) {
        Optional<Course> optionalCourse = courseService.addCourse(course);
        optionalCourse.orElseThrow(() -> new RestException("error", HttpStatus.CONFLICT));
        return ApiResult.successResponse("new course added");
    }

    @CheckPermission(permission = {"EDIT_USER"})
    @PutMapping()
    public ApiResult<?> editCourse(@Valid @RequestBody Course course) {
        Optional<Course> optionalCourse = courseService.editCourse(course);
        optionalCourse.orElseThrow(() -> new RestException("error", HttpStatus.CONFLICT));
        return ApiResult.successResponse("Course edited");
    }

    @GetMapping("/{id}")
    public ApiResult<Course> getCourse(@PathVariable Integer id) {
        Course course = courseService.getCourse(id);
        return ApiResult.successResponse(course);
    }

    @GetMapping
    public ApiResult<List<Course>> getCourses() {
        List<Course> courses = courseService.getCourses();
        return ApiResult.successResponse(courses);
    }

    @GetMapping("/ketmon")
    public ApiResult<String> getKetmon() {
        return ApiResult.successResponse("Qalaysan");
    }

    @CheckPermission(permission = {"DELETE_COURSE"})
    @DeleteMapping("/{id}")
    public ApiResult<?> deleteCourse(@PathVariable Integer id) {
        ApiResult<Boolean> booleanApiResult = courseService.deleteCourse(id);
        return booleanApiResult;
    }

}
