package ai.ecma.appintegrationtest.repository;
import ai.ecma.appintegrationtest.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Module,Integer> {

    boolean existsByName( String name);

}
