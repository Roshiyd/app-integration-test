package ai.ecma.appintegrationtest.service;

import ai.ecma.appintegrationtest.entity.Course;
import ai.ecma.appintegrationtest.entity.Module;
import ai.ecma.appintegrationtest.payload.ApiResult;
import ai.ecma.appintegrationtest.payload.ModuleDTO;
import ai.ecma.appintegrationtest.payload.RestException;
import ai.ecma.appintegrationtest.repository.CourseRepository;
import ai.ecma.appintegrationtest.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

@Service
public class ModuleService {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    CourseRepository courseRepository;
    public Optional<Module> addModule(ModuleDTO moduleDTO) {

        boolean existsByName = moduleRepository.existsByName(moduleDTO.getName());

        if (existsByName){
            throw new RestException("Bunday nomli Module allaqachon mavjud", HttpStatus.CONFLICT);
        }
        
        Optional<Course> optionalCourse = courseRepository.findById(moduleDTO.getCourseId());

        if (optionalCourse.isEmpty()){
            throw new RestException("Bunday Course topilamdi",HttpStatus.NOT_FOUND);
        }

        Module module = new Module();
        module.setCourse(optionalCourse.get());
        module.setDescription(moduleDTO.getDescription());
        module.setName(moduleDTO.getName());
        module.setOrders(moduleDTO.getOrder());
        module.setPrice(moduleDTO.getPrice());

        Module save = moduleRepository.save(module);

        return  Optional.of(save);

    }

    public ApiResult<Boolean> editModule(ModuleDTO moduleDTO) {

        Optional<Module> optionalModule = moduleRepository.findById(moduleDTO.getId());

        if (optionalModule.isEmpty()){
            throw new RestException("Bunday module topilmadi",HttpStatus.NOT_FOUND);
        }
        Optional<Course> optionalCourse = courseRepository.findById(moduleDTO.getCourseId());

        if (optionalCourse.isEmpty()){
            throw new RestException("Bunday Course topilamdi",HttpStatus.NOT_FOUND);
        }

        Module module = optionalModule.get();

        module.setPrice(moduleDTO.getPrice());
        module.setName(moduleDTO.getName());
        module.setDescription(moduleDTO.getDescription());
        module.setOrders(moduleDTO.getOrder());
        module.setCourse(optionalCourse.get());

        moduleRepository.save(module);

        return ApiResult.successResponse();


    }


    public Module getModule(Integer id) {

        Optional<Module> optionalModule = moduleRepository.findById(id);
        if (optionalModule.isEmpty()){
            throw new RestException("Bunday Module topilamadi",HttpStatus.NOT_FOUND);
        }

        return optionalModule.get();

    }

    public List<Module> getModules() {

       return moduleRepository.findAll();

    }

    public ApiResult<Boolean> deleteModule(Integer id) {

        Optional<Module> optionalModule = moduleRepository.findById(id);
        if (optionalModule.isEmpty()){
            throw new RestException("Bunday Module topilamdi",HttpStatus.NOT_FOUND);
        }

        moduleRepository.deleteById(id);
        return ApiResult.successResponse(true);



    }
}
