import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal class IntegrationTest {
    private lateinit var stdoutBuffer: ByteArrayOutputStream
    private lateinit var originalStdout: PrintStream

    @BeforeEach
    fun before() {
        stdoutBuffer = ByteArrayOutputStream()
        originalStdout = System.out
        System.setOut(PrintStream(stdoutBuffer))
    }

    @AfterEach
    fun after() {
        System.setOut(originalStdout)
    }

    @Test
    fun `Should parse config & print expected output`() {
        // Given
        val filePath = "src/test/resources/test_config.txt"
        val currentTime = "16:10"
        val args = arrayOf(filePath, currentTime)

        // When
        main(args)

        // Then
        with(stdoutBuffer.toString().split("\n")) {
            assertEquals(5, size)
            assertEquals("01:30 tomorrow - /bin/run_me_daily", this[0])
            assertEquals("16:45 today - /bin/run_me_hourly", this[1])
            assertEquals("16:10 today - /bin/run_me_every_minute", this[2])
            assertEquals("19:00 today - /bin/run_me_sixty_times", this[3])
            assertEquals("", this[4])
        }
    }
}
