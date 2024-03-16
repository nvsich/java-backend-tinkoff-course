package edu.java.scrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MigrationTest extends IntegrationTest {

    @Test
    public void migrationIsRunningTest() {
        assertTrue(IntegrationTest.POSTGRES.isRunning());
    }
}
