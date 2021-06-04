package ai.ecma.appintegrationtest.controller;

import ai.ecma.appintegrationtest.aop.CheckPermission;
import ai.ecma.appintegrationtest.entity.Lesson;
import ai.ecma.appintegrationtest.entity.Module;
import ai.ecma.appintegrationtest.payload.ApiResult;
import ai.ecma.appintegrationtest.payload.LessonDTO;
import ai.ecma.appintegrationtest.payload.ModuleDTO;
import ai.ecma.appintegrationtest.payload.RestException;
import ai.ecma.appintegrationtest.service.LessonService;
import ai.ecma.appintegrationtest.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(LessonController.LESSON_URL)
public class LessonController {

    public static final String  LESSON_URL = "/api/lesson";

    @Autowired
    LessonService lessonService;

    @CheckPermission(permission = {"ADD_LESSON"})
    @PostMapping()
    public ApiResult<?> addLesson(@Valid @RequestBody LessonDTO lessonDTO){
        Optional<Lesson> lesson = lessonService.addLesson(lessonDTO);
         lesson.orElseThrow(() -> new RestException("error", HttpStatus.CONFLICT));
        return ApiResult.successResponse("new Lesson added");
    }

    @CheckPermission(permission = {"EDIT_LESSON"})
    @PutMapping
    public ApiResult<Boolean> editLesson(@Valid @RequestBody LessonDTO lessonDTO){

        ApiResult<Boolean> booleanApiResult = lessonService.editLesson(lessonDTO);

        return booleanApiResult;
    }

    @GetMapping("/{id}")
    public ApiResult<Lesson> getLesson(@PathVariable Integer id){

        Lesson lesson = lessonService.getLesson(id);

        return ApiResult.successResponse(lesson);
    }

    @GetMapping
    public ApiResult<List<Lesson>> getLessons(){

        List<Lesson> lessons = lessonService.getLessons();

        return ApiResult.successResponse(lessons);

    }

    @CheckPermission(permission = {"DELETE_LESSON"})
    @DeleteMapping("/{id}")
    public ApiResult<?> deleteModule(@PathVariable Integer id){

        ApiResult<Boolean> booleanApiResult =lessonService.deleteLesson(id);
        return booleanApiResult;

    }
}
