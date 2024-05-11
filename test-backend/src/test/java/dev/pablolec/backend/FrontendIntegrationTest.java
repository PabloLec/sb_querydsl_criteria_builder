package dev.pablolec.backend;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class FrontendIntegrationTest extends AbstractIntegrationTest {
    @LocalServerPort
    private int port;

    private static DockerComposeContainer<?> environment;

    @BeforeEach
    void startFrontend() {
        Path currentWorkingDirectory = Paths.get(System.getProperty("user.dir"));

        Path composeFilePath = currentWorkingDirectory.getParent().resolve("test-frontend/docker-compose.yml");

        File composeFile = composeFilePath.toFile();
        if (!composeFile.exists()) {
            throw new RuntimeException("Docker Compose file not found at path: " + composeFile.getAbsolutePath());
        }

        String backendUrl = "http://localhost:" + port;

        environment = new DockerComposeContainer<>(composeFile)
                .withEnv("BACKEND_URL", backendUrl)
                .withExposedService("frontend", 4173);
        environment.start();
    }

    @AfterEach
    void stopFrontend() {
        environment.stop();
    }

    @Test
    void frontendTest() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch();
            Page page = browser.newPage();
            page.navigate("http://localhost:4173");
        }
    }
}
