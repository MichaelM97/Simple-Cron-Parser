import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.PrintStream

internal class EndToEndTest {
    private lateinit var originalStdin: InputStream
    private lateinit var stdoutBuffer: ByteArrayOutputStream
    private lateinit var originalStdout: PrintStream

    @BeforeEach
    fun before() {
        originalStdin = System.`in`
        stdoutBuffer = ByteArrayOutputStream()
        originalStdout = System.out
        System.setOut(PrintStream(stdoutBuffer))
    }

    @AfterEach
    fun after() {
        System.setIn(originalStdin)
        System.setOut(originalStdout)
    }

    @Test
    fun `Should parse config & print expected output`() {
        // Given
        val inputString = "30 1 /bin/run_me_daily\n" +
                "45 * /bin/run_me_hourly\n" +
                "* * /bin/run_me_every_minute\n" +
                "* 19 /bin/run_me_sixty_times"
        val inputStream = ByteArrayInputStream(inputString.toByteArray())
        System.setIn(inputStream)
        val args = arrayOf("16:10")

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

    @Test
    fun `Should print error when stdin is empty`() {
        // Given
        val inputString = ""
        val inputStream = ByteArrayInputStream(inputString.toByteArray())
        System.setIn(inputStream)
        val args = arrayOf("16:10")

        // When
        main(args)

        // Then
        with(stdoutBuffer.toString().split("\n")) {
            assertEquals(2, size)
            assertEquals("No valid config entries were found in stdin.", this[0])
            assertEquals("", this[1])
        }
    }

    @Test
    fun `Should print error when args is empty`() {
        // Given
        val inputString = "30 1 /bin/run_me_daily\n" +
                "45 * /bin/run_me_hourly\n" +
                "* * /bin/run_me_every_minute\n" +
                "* 19 /bin/run_me_sixty_times"
        val inputStream = ByteArrayInputStream(inputString.toByteArray())
        System.setIn(inputStream)
        val args = arrayOf<String>()

        // When
        main(args)

        // Then
        with(stdoutBuffer.toString().split("\n")) {
            assertEquals(2, size)
            assertEquals("Not enough arguments passed, you must provide the current time.", this[0])
            assertEquals("", this[1])
        }
    }
}
