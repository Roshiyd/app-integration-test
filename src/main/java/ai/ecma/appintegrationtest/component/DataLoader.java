package ai.ecma.appintegrationtest.component;

import ai.ecma.appintegrationtest.entity.Role;
import ai.ecma.appintegrationtest.entity.User;
import ai.ecma.appintegrationtest.entity.enums.Permission;
import ai.ecma.appintegrationtest.repository.RoleRepository;
import ai.ecma.appintegrationtest.repository.UserRepository;
import ai.ecma.appintegrationtest.utils.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

import static ai.ecma.appintegrationtest.entity.enums.Permission.*;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Value("${spring.sql.init.enabled}")
    private Boolean initialMode;

    @Override
    public void run(String... args) throws Exception {
        if (initialMode) {
            saveUsers();
        }


    }

    public void saveUsers() {
        Permission[] permissions = Permission.values();

        Role admin = roleRepository.save(new Role(
                RestConstants.ADMIN,
                Arrays.asList(permissions),
                "Admin of system"
        ));
        Role user = roleRepository.save(new Role(
                RestConstants.USER,
                Collections.emptyList(),
                "Simple user"
        ));

        Role employee = roleRepository.save(new Role(
                RestConstants.EMPLOYEE,
                Arrays.asList(ADD_COURSE, EDIT_COURSE, DELETE_COURSE, ADD_MODULE, EDIT_MODULE, DELETE_MODULE,
                        ADD_LESSON, EDIT_LESSON, DELETE_LESSON),
                "Employee which can add,edit,delete course,module and lessons "
        ));

        userRepository.save(new User(
                "admin",
                passwordEncoder.encode("admin123"),
                admin,
                true
        ));

        userRepository.save(new User(
                "user",
                passwordEncoder.encode("user123"),
                user,
                true
        ));
        userRepository.save(new User(
                "employee",
                passwordEncoder.encode("employee123"),
                employee,
                true
        ));
    }
}
