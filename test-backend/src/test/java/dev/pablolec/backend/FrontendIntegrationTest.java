package dev.pablolec.backend;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import dev.pablolec.backend.db.model.Book;
import dev.pablolec.backend.db.model.Library;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class FrontendIntegrationTest extends AbstractIntegrationTest {
    private static DockerComposeContainer<?> environment;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    @BeforeAll
    static void beforeAll() {
        Path currentWorkingDirectory = Paths.get(System.getProperty("user.dir"));
        Path composeFilePath = currentWorkingDirectory.getParent().resolve("test-frontend/docker-compose.yml");
        File composeFile = composeFilePath.toFile();
        if (!composeFile.exists()) {
            throw new RuntimeException("Docker Compose file not found at path: " + composeFile.getAbsolutePath());
        }

        environment = new DockerComposeContainer<>(composeFile)
                .withEnv("BACKEND_URL", "http://localhost:8090")
                .withExposedService("frontend", 4173);
        environment.start();

        browser = Playwright.create().firefox().launch();
    }

    @AfterAll
    static void afterAll() {
        browser.close();
        environment.stop();
    }

    @BeforeEach
    void beforeEach() {
        insertTestData();
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));
        page = context.newPage();
        page.navigate("http://localhost:4173");
    }

    @AfterEach
    void afterEach() {
        context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("/tmp/trace.zip")));
        page.close();
        browser.contexts().forEach(BrowserContext::close);
    }

    @Test
    void frontendSearchScenarioTest() {
        // Add first criterion
        Locator addCriterionButton = page.getByTestId("library-add-criterion-button");
        assertThat(addCriterionButton).isVisible();
        addCriterionButton.click();

        // Open field selection combobox
        Locator firstLibraryField = page.getByTestId("library-field-0");
        assertThat(firstLibraryField).isVisible();
        firstLibraryField.click();

        // Click on 'Name' field
        Locator firstLibraryFieldComboBoxPopover = page.getByTestId("library-field-0-popover");
        Locator nameOption =
                firstLibraryFieldComboBoxPopover.locator("[role=option]").getByText("Name");
        assertThat(nameOption).isVisible();
        nameOption.click();
        assertThat(firstLibraryField).hasText("Name");

        // Operation and value fields should be visible
        Locator firstLibraryOperation = page.getByTestId("library-operation-0");
        assertThat(firstLibraryOperation).isVisible();
        Locator firstLibraryValue = page.getByTestId("library-value-0-input");
        assertThat(firstLibraryValue).isVisible();

        // Select operation 'Contains'
        firstLibraryOperation.click();
        Locator firstLibraryOperationSelect = firstLibraryOperation.locator("select");
        assertThat(firstLibraryOperationSelect).isVisible();
        Locator firstLibraryOperationContains = page.getByLabel("contains").getByText("contains");
        assertThat(firstLibraryOperationContains).isVisible();
        firstLibraryOperationContains.click();
        Locator firstLibraryValueButton = firstLibraryOperation.locator("button");
        assertThat(firstLibraryValueButton).hasText("contains");

        // Enter value 'Library'
        firstLibraryValue.click();
        firstLibraryValue.fill("Library");
        assertThat(firstLibraryValue).hasValue("Library");

        // Add second criterion
        addCriterionButton = page.getByTestId("library-add-criterion-button");
        assertThat(addCriterionButton).isVisible();
        addCriterionButton.click();

        // Open field selection combobox
        Locator secondLibraryField = page.getByTestId("library-field-1");
        assertThat(secondLibraryField).isVisible();
        secondLibraryField.click();

        // Click on 'Book' field
        Locator secondLibraryFieldComboBoxPopover = page.getByTestId("library-field-1-popover");
        Locator bookOption =
                secondLibraryFieldComboBoxPopover.locator("[role=option]").getByText("Book");
        assertThat(bookOption).isVisible();
        bookOption.click();
        assertThat(secondLibraryField).hasText("Book");

        // Only operation field should be visible with default value 'exists'
        Locator secondLibraryOperation = page.getByTestId("library-operation-1");
        assertThat(secondLibraryOperation).isVisible();
        Locator secondLibraryValueButton = secondLibraryOperation.locator("button");
        assertThat(secondLibraryValueButton).hasText("exists");
        assertThat(page.getByTestId("library-value-1-input")).hasCount(0);

        // Add sub-criterion
        Locator addSubCriterionButton = page.getByTestId("book-add-sub-criterion-button");
        assertThat(addSubCriterionButton).isVisible();
        addSubCriterionButton.click();

        // Open field selection combobox
        Locator firstBookField = page.getByTestId("book-field-0");
        assertThat(firstBookField).isVisible();
        firstBookField.click();

        // Click on 'Title' field
        Locator firstBookFieldComboBoxPopover = page.getByTestId("book-field-0-popover");
        Locator titleOption =
                firstBookFieldComboBoxPopover.locator("[role=option]").getByText("Title");
        assertThat(titleOption).isVisible();
        titleOption.click();
        assertThat(firstBookField).hasText("Title");

        // Operation and value fields should be visible
        Locator firstBookOperation = page.getByTestId("book-operation-0");
        assertThat(firstBookOperation).isVisible();
        Locator firstBookValue = page.getByTestId("book-value-0-input");
        assertThat(firstBookValue).isVisible();

        // Select operation 'equals to'
        firstBookOperation.click();
        Locator firstBookOperationSelect = firstBookOperation.locator("select");
        assertThat(firstBookOperationSelect).isVisible();
        Locator firstBookOperationDoesNotContain =
                page.getByLabel("equals to", new Page.GetByLabelOptions().setExact(true));
        assertThat(firstBookOperationDoesNotContain).isVisible();
        firstBookOperationDoesNotContain.click();
        Locator firstBookValueButton = firstBookOperation.locator("button");
        assertThat(firstBookValueButton).hasText("equals to");

        // Enter value 'Java Basics'
        firstBookValue.click();
        firstBookValue.fill("Java Basics");
        assertThat(firstBookValue).hasValue("Java Basics");

        // Click on 'Search' button
        Locator searchButton = page.getByTestId("search-button");
        assertThat(searchButton).isVisible();
        searchButton.click();

        // Check if the search results are displayed
        Locator searchResults = page.getByTestId("search-results");
        assertThat(searchResults).isVisible();

        // Check if correct Library name is displayed
        assertThat(searchResults.getByRole(AriaRole.CELL, new Locator.GetByRoleOptions().setName("National Library")))
                .isVisible();
    }

    private void insertTestData() {
        Library library = Library.builder()
                .name("National Library")
                .location("Paris")
                .openingHours("09:00-17:00")
                .establishedDate(LocalDate.of(1884, 1, 1))
                .website("http://bnf.fr")
                .email("info@bnf.fr")
                .phoneNumber("0123456789")
                .isOpen(true)
                .build();
        libraryRepository.save(library);

        Book book = Book.builder()
                .title("Java Basics")
                .isbn("123456789")
                .publishYear(2020)
                .edition("3rd")
                .language("English")
                .genre("Education")
                .library(library)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        bookRepository.save(book);
    }
}
