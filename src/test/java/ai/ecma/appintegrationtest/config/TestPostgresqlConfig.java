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

    /*private static final List<String> DEFAULT_ADDITIONAL_INIT_DB_PARAMS = Arrays
            .asList("--nosync", "--locale=en_US.UTF-8");*/
    @Autowired
    Environment env;

    PostgresProcess embeddedPostgres;

    /**
     * @return PostgresProcess , the started db process
     */
    @Bean
    @DependsOn("postgresProcess")
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(Objects.requireNonNull(env.getProperty("hibernate.connection.driver")));
        ds.setUsername(env.getProperty("hibernate.connection.username"));
        ds.setPassword(env.getProperty("hibernate.connection.password"));
        ds.setUrl(env.getProperty("hibernate.connection.url"));
        return ds;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter theVendorAdapter = new HibernateJpaVendorAdapter();
        theVendorAdapter.setDatabasePlatform(env.getProperty("hibernate.connection.dialect"));
        theVendorAdapter.setShowSql("true".equalsIgnoreCase(env.getProperty("hibernate.showsql")));
        theVendorAdapter.setGenerateDdl(Boolean.TRUE);
        theVendorAdapter.setDatabase(Database.valueOf(Objects.requireNonNull(env.getProperty("hibernate.dbtype"))));
        return theVendorAdapter;
    }

    @Bean
    public PostgresConfig postgresConfig() throws IOException {
        final PostgresConfig postgresConfig = new PostgresConfig(Version.V9_6_11,
//                new AbstractPostgresConfig.Net("localhost", Network.getFreeServerPort()),
                new AbstractPostgresConfig.Net("localhost", 5435), //this is port for embedded virtual database see test profile properties
                new AbstractPostgresConfig.Storage("postgres"),
                new AbstractPostgresConfig.Timeout(),
//                new AbstractPostgresConfig.Credentials(env.getProperty("hibernate.connection.username"), env.getProperty("hibernate.connection.password"))
                new AbstractPostgresConfig.Credentials("postgres","root123")
        );
//        postgresConfig.getAdditionalInitDbParams().addAll(DEFAULT_ADDITIONAL_INIT_DB_PARAMS);
        return postgresConfig;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan("ai.ecma.appintegrationtest.entity");
        entityManagerFactory.setPersistenceUnitName("postgres");
        /*theEntityManager.setPersistenceUnitName(configrationProperties
                .getString("db.jpa.persistanceUnit"));*/
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());

        HashMap<String, Object> properties = new HashMap<>();

        // JPA & Hibernate
        properties.put("hibernate.dialect", env.getProperty("hibernate.connection.dialect"));
        properties.put("hibernate.show-sql", Boolean.TRUE);
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
//        properties.put("useUnicode", Boolean.TRUE);
//        properties.put("characterEncoding", "UTF-8");

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
