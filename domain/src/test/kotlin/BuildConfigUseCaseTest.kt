import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import models.Config
import models.ConfigLine
import models.ConfigTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class BuildConfigUseCaseTest {
    private val mockConfigRepository: ConfigRepository = mockk()
    private lateinit var buildConfigUseCase: BuildConfigUseCase

    @BeforeEach
    fun before() {
        buildConfigUseCase = BuildConfigUseCase(
            configRepository = mockConfigRepository,
        )
    }

    @Test
    fun `Should return success when repository is successful`() {
        // Given
        val configLines = listOf(
            ConfigLine(
                minutes = ConfigTime.All,
                hour = ConfigTime.Value(12),
                command = "run_me_at_12",
            ),
            ConfigLine(
                minutes = ConfigTime.All,
                hour = ConfigTime.All,
                command = "run_me_at_always",
            ),
        )
        every { mockConfigRepository.getConfigLines() } returns Result.success(configLines)

        // When
        val result = buildConfigUseCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(Config(configLines = configLines), result.getOrThrow())
    }

    @Test
    fun `Should return failure when repository fails`() {
        // Given
        every { mockConfigRepository.getConfigLines() } returns Result.failure(Exception())

        // When
        val result = buildConfigUseCase()

        // Then
        assertTrue(result.isFailure)
    }
}
