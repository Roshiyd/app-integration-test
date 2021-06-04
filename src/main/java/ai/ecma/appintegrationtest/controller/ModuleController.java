package ai.ecma.appintegrationtest.controller;

import ai.ecma.appintegrationtest.aop.CheckPermission;
import ai.ecma.appintegrationtest.entity.Course;
import ai.ecma.appintegrationtest.entity.Module;
import ai.ecma.appintegrationtest.payload.ApiResult;
import ai.ecma.appintegrationtest.payload.ModuleDTO;
import ai.ecma.appintegrationtest.payload.RestException;
import ai.ecma.appintegrationtest.service.CourseService;
import ai.ecma.appintegrationtest.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ModuleController.MODULE_URL)
public class ModuleController {

    public static final String  MODULE_URL = "/api/module";


    @Autowired
    ModuleService moduleService;
    @CheckPermission(permission = {"ADD_MODULE"})
    @PostMapping()
    public ApiResult<?> addModule(@Valid @RequestBody ModuleDTO moduleDTO){
        Optional<Module> module = moduleService.addModule(moduleDTO);
        module.orElseThrow(() -> new RestException("error", HttpStatus.CONFLICT));
        return ApiResult.successResponse("new module added");
    }

    @CheckPermission(permission = {"EDIT_MODULE"})
    @PutMapping
    public ApiResult<Boolean> editModule(@Valid @RequestBody ModuleDTO moduleDTO){

        ApiResult<Boolean> booleanApiResult = moduleService.editModule(moduleDTO);

        return booleanApiResult;
    }

    @GetMapping("/{id}")
    public ApiResult<Module> getModule(@PathVariable Integer id){

        Module module = moduleService.getModule(id);

        return ApiResult.successResponse(module);
    }

    @GetMapping
    public ApiResult<List<Module>> getModules(){
        List<Module> modules = moduleService.getModules();
        return ApiResult.successResponse(modules);
    }

    @CheckPermission(permission = {"DELETE_MODULE"})
    @DeleteMapping("/{id}")
    public ApiResult<?> deleteModule(@PathVariable Integer id){

        ApiResult<Boolean> booleanApiResult = moduleService.deleteModule(id);
        return booleanApiResult;

    }

}
