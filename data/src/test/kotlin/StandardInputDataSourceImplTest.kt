import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class StandardInputDataSourceImplTest {
    private lateinit var standardInputDataSourceImpl: StandardInputDataSourceImpl

    @BeforeEach
    fun before() {
        mockkStatic("kotlin.io.ConsoleKt")
        standardInputDataSourceImpl = StandardInputDataSourceImpl()
    }

    @Nested
    inner class ReadNextLine {
        @Test
        fun `Should return next line`() {
            // Given
            val line = "45 * /bin/run_me_hourly"
            every { readlnOrNull() } returns line

            // When
            val result = standardInputDataSourceImpl.readNextLine()

            // Then
            assertEquals(line, result)
        }

        @Test
        fun `Should return null`() {
            // Given
            every { readlnOrNull() } returns null

            // When
            val result = standardInputDataSourceImpl.readNextLine()

            // Then
            assertEquals(null, result)
        }
    }
}
