package ai.ecma.appintegrationtest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;
import ru.yandex.qatools.embed.postgresql.distribution.Version;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@ActiveProfiles(profiles = "test")
@Configuration
public class TestPostgresqlConfig {

    @Autowired
    Environment env;

    PostgresProcess embeddedPostgres;

    /**
     * @return PostgresProcess , the started db process
     */
    @Bean
    @DependsOn("postgresProcess")
    public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.datasource.driver-class-name")));
        driverManagerDataSource.setUsername(env.getProperty("spring.datasource.username"));
        driverManagerDataSource.setPassword(env.getProperty("spring.datasource.password"));
        driverManagerDataSource.setUrl(env.getProperty("spring.datasource.url"));
        return driverManagerDataSource;

    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setDatabasePlatform(env.getProperty("hibernate.connection.dialect"));
        hibernateJpaVendorAdapter.setShowSql("true".equalsIgnoreCase(env.getProperty("spring.jpa.show-sql")));
        hibernateJpaVendorAdapter.setGenerateDdl(Boolean.TRUE);
        hibernateJpaVendorAdapter.setDatabase(Database.valueOf(Objects.requireNonNull(env.getProperty("hibernate.dbtype"))));
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public PostgresConfig postgresConfig() throws IOException {
        return new PostgresConfig(Version.V11_1,
                new AbstractPostgresConfig.Net(env.getProperty("spring.datasource.host"), Integer.parseInt(env.getProperty("spring.datasource.port"))), //this is port for embedded virtual database see test profile properties
                new AbstractPostgresConfig.Storage(env.getProperty("spring.datasource.dbName")),
                new AbstractPostgresConfig.Timeout(),
                new AbstractPostgresConfig.Credentials(env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"))
        );
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan("ai.ecma.appintegrationtest.entity");
        entityManagerFactory.setPersistenceUnitName("postgres");
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());

        HashMap<String, Object> properties = new HashMap<>();
        // JPA & Hibernate
        properties.put("hibernate.dialect", env.getProperty("hibernate.connection.dialect"));
        entityManagerFactory.setJpaPropertyMap(properties);
        entityManagerFactory.afterPropertiesSet();
        return entityManagerFactory;
    }


    @Bean(destroyMethod = "stop")
    public PostgresProcess postgresProcess(PostgresConfig config) throws IOException {
        PostgresStarter<PostgresExecutable, PostgresProcess> runtime = PostgresStarter.getDefaultInstance();
        PostgresExecutable exec = runtime.prepare(config);
        embeddedPostgres = exec.start();
        return embeddedPostgres;
    }
}
