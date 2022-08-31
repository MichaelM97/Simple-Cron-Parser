import models.CurrentTime
import models.InputArgs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class ParseInputArgsUseCaseTest {
    private lateinit var parseInputArgsUseCase: ParseInputArgsUseCase

    @BeforeEach
    fun before() {
        parseInputArgsUseCase = ParseInputArgsUseCase()
    }

    @Test
    fun `Should parse valid input args and return`() {
        // Given
        val args = arrayOf("16:10")

        // When
        val result = parseInputArgsUseCase(args)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(
            InputArgs(currentTime = CurrentTime(hour = 16, minute = 10)),
            result.getOrThrow(),
        )
    }

    @Test
    fun `Should fail if no args passed`() {
        // Given
        val args = arrayOf<String>()

        // When
        val result = parseInputArgsUseCase(args)

        // Then
        assertTrue(result.isFailure)
    }

    @ParameterizedTest
    @ValueSource(strings = ["0", "invalid_time", "12:", "28:30", "12:72", "80:76"])
    fun `Should fail if invalid time arg passed`(invalidTimeArg: String) {
        // Given
        val args = arrayOf(invalidTimeArg)

        // When
        val result = parseInputArgsUseCase(args)

        // Then
        assertTrue(result.isFailure)
    }
}
