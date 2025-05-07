package com.example.uhabmessenger.bd.init;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(
        properties = {
                "spring.profiles.active=test",
                "server.port=8008", // Фиксированный порт для веб-приложения
                "server.servlet.context-path=/uhub"
        }
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class BaseIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.datasource.type", () -> "com.zaxxer.hikari.HikariDataSource"); // Явно указываем Hikari
        registry.add("spring.liquibase.enabled", () -> "true"); // Включаем Liquibase для тестов

        // MinIO properties
        registry.add("minio.url", () -> "http://localhost:9000");
        registry.add("minio.access-key", () -> "minio_user");
        registry.add("minio.secret-key", () -> "password123");
        registry.add("minio.bucket-name", () -> "photo-bucket");
    }
}
