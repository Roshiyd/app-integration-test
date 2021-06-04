package ai.ecma.appintegrationtest.aop;

import ai.ecma.appintegrationtest.entity.User;
import ai.ecma.appintegrationtest.payload.ForbiddenException;
import ai.ecma.appintegrationtest.payload.RestException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class CheckPermissionExecutor {

    @Before(value = "@annotation(checkPermission)")
    public void checkUserPermissionMethod(CheckPermission checkPermission){
        User user=(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean exist=false;
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (Arrays.asList(authority.getAuthority()).containsAll(Arrays.asList(checkPermission.permission()))){
                exist=true;
                break;
            }
        }
        if (!exist){
            throw new RestException("Permission mavjud emas", HttpStatus.FORBIDDEN);
        }
    }
}
