package ai.ecma.appintegrationtest.service;

import ai.ecma.appintegrationtest.entity.Course;
import ai.ecma.appintegrationtest.entity.Lesson;
import ai.ecma.appintegrationtest.entity.Module;
import ai.ecma.appintegrationtest.payload.ApiResult;
import ai.ecma.appintegrationtest.payload.LessonDTO;
import ai.ecma.appintegrationtest.payload.RestException;
import ai.ecma.appintegrationtest.repository.LessonRepository;
import ai.ecma.appintegrationtest.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {

    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    ModuleRepository moduleRepository;

    public Optional<Lesson> addLesson(LessonDTO lessonDTO) {

        boolean existsByName = lessonRepository.existsByName(lessonDTO.getName());
        if (existsByName){
            throw new RestException("Bunday lesson allaqachon mavjud ", HttpStatus.CONFLICT);
        }

        Optional<Module> optionalModule = moduleRepository.findById(lessonDTO.getModuleId());
        if (!optionalModule.isPresent()){
            throw new RestException("Bunday Module toplimadi",HttpStatus.NOT_FOUND);
        }

        Lesson lesson = new Lesson();
        lesson.setDescription(lessonDTO.getDescription());
        lesson.setName(lessonDTO.getName());
        lesson.setOrders(lessonDTO.getOrder());
        lesson.setModule(optionalModule.get());

        Lesson save = lessonRepository.save(lesson);

        return  Optional.of(save);

    }

    public ApiResult<Boolean> editLesson(LessonDTO lessonDTO) {

        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonDTO.getId());

        if (!optionalLesson.isPresent()){
            throw new RestException("Bunday lesson topilmadi",HttpStatus.NOT_FOUND);
        }
        Optional<Module> optionalModule = moduleRepository.findById(lessonDTO.getModuleId());

        if (!optionalModule.isPresent()){
            throw new RestException("Bunday Module topilamdi",HttpStatus.NOT_FOUND);
        }

        Lesson lesson = optionalLesson.get();

       lesson.setModule(optionalModule.get());
       lesson.setName(lessonDTO.getName());
       lesson.setOrders(lessonDTO.getOrder());
       lesson.setDescription(lessonDTO.getDescription());

       lessonRepository.save(lesson);

        return ApiResult.successResponse();

    }

    public Lesson getLesson(Integer id) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(id);
        if (optionalLesson.isEmpty()){
            throw new RestException("Bunday Lesson topilamadi",HttpStatus.NOT_FOUND);
        }

        return optionalLesson.get();

    }

    public List<Lesson> getLessons() {
        return lessonRepository.findAll();

    }

    public ApiResult<Boolean> deleteLesson(Integer id) {

        Optional<Lesson> optionalLesson = lessonRepository.findById(id);
        if (!optionalLesson.isPresent()){
            throw new RestException("Bunday Lesson topilamdi",HttpStatus.NOT_FOUND);
        }

        lessonRepository.deleteById(id);
        return ApiResult.successResponse(true);

    }
}
