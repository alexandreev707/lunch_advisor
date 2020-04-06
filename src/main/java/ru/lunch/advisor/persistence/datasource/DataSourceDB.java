package ru.lunch.advisor.persistence.datasource;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Кофигурация {@link DataSource} для {@link Liquibase}
 */
@Configuration
public class DataSourceDB extends DriverManagerDataSource {

    private static Logger log = LoggerFactory.getLogger(DataSourceDB.class);

    @Bean
    public DataSource dataSource(@Value("${jdbc.driver.class.name}") String driverClassName,
                                 @Value("${jdbc.url}") String jdbcUrl,
                                 @Value("${jdbc.user}") String jdbcUser,
                                 @Value("${jdbc.min.idle}") Integer minIdle,
                                 @Value("${jdbc.max.pool}") Integer maxPool) throws SQLException, LiquibaseException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(jdbcUser);
        dataSource.setMinimumIdle(minIdle);
        dataSource.setMaximumPoolSize(maxPool);

        try (Connection connection = dataSource.getConnection()) {
            Liquibase liquibase = new Liquibase("db/changelog/changelog-master.xml",
                    new ClassLoaderResourceAccessor(), new JdbcConnection(connection));
            liquibase.update(new Contexts());
        } catch (LiquibaseException e) {
            log.error("Database migration error: ", e);
            throw e;
        }

        return dataSource;
    }
}
