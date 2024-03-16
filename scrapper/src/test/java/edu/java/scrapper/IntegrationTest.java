package edu.java.scrapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import liquibase.Liquibase;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@Testcontainers
public abstract class IntegrationTest {
    public static PostgreSQLContainer<?> POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");
        POSTGRES.start();

        runMigrations(POSTGRES);
    }

    private static void runMigrations(JdbcDatabaseContainer<?> container) {
        Path changelogPath = new File(".")
            .toPath()
            .toAbsolutePath()
            .resolve("../migrations");

        String masterXmlPath = "master.xml";

        try (Connection connection = DriverManager.getConnection(
            container.getJdbcUrl(),
            container.getUsername(),
            container.getPassword()
        )) {
            Database database = DatabaseFactory
                .getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                masterXmlPath,
                new DirectoryResourceAccessor(changelogPath),
                database
            );

            CommandScope updateCommand = new CommandScope(UpdateCommandStep.COMMAND_NAME);

            updateCommand
                .addArgumentValue(DbUrlConnectionCommandStep.DATABASE_ARG, liquibase.getDatabase())
                .addArgumentValue(UpdateCommandStep.CHANGELOG_ARG, liquibase.getDatabaseChangeLog())
                .execute();

        } catch (SQLException | FileNotFoundException | LiquibaseException e) {
            log.error(e.getMessage());
        }
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
}
