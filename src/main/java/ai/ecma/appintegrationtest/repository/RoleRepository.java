package ai.ecma.appintegrationtest.repository;

import ai.ecma.appintegrationtest.entity.Lesson;
import ai.ecma.appintegrationtest.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(String name);
    boolean existsByName( String name);

}
