import models.Config
import models.ConfigLine
import models.ConfigTime
import models.CurrentTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BuildConfigOutputUseCaseTest {
    private lateinit var buildConfigOutputUseCase: BuildConfigOutputUseCase

    @BeforeEach
    fun before() {
        buildConfigOutputUseCase = BuildConfigOutputUseCase()
    }

    @Test
    fun `Should build expected output`() {
        // Given
        val currentTime = CurrentTime(hour = 16, minute = 10)

        // When
        val output = buildConfigOutputUseCase(CONFIG, currentTime)

        // Then
        assertEquals(
            listOf(
                "01:30 tomorrow - /bin/run_me_daily",
                "16:45 today - /bin/run_me_hourly",
                "16:10 today - /bin/run_me_every_minute",
                "19:00 today - /bin/run_me_sixty_times",
            ),
            output,
        )
    }

    @Test
    fun `Should build expected output when time is 23 59`() {
        // Given
        val currentTime = CurrentTime(hour = 23, minute = 59)

        // When
        val output = buildConfigOutputUseCase(CONFIG, currentTime)

        // Then
        assertEquals(
            listOf(
                "01:30 tomorrow - /bin/run_me_daily",
                "00:45 tomorrow - /bin/run_me_hourly",
                "23:59 today - /bin/run_me_every_minute",
                "19:00 tomorrow - /bin/run_me_sixty_times",
            ),
            output,
        )
    }

    @Test
    fun `Should build expected output when time is 00 00`() {
        // Given
        val currentTime = CurrentTime(hour = 0, minute = 0)

        // When
        val output = buildConfigOutputUseCase(CONFIG, currentTime)

        // Then
        assertEquals(
            listOf(
                "01:30 today - /bin/run_me_daily",
                "00:45 today - /bin/run_me_hourly",
                "00:00 today - /bin/run_me_every_minute",
                "19:00 today - /bin/run_me_sixty_times",
            ),
            output,
        )
    }

    private companion object {
        val CONFIG = Config(
            configLines = listOf(
                ConfigLine(minutes = ConfigTime.Value(30), hour = ConfigTime.Value(1), command = "/bin/run_me_daily"),
                ConfigLine(minutes = ConfigTime.Value(45), hour = ConfigTime.All, command = "/bin/run_me_hourly"),
                ConfigLine(minutes = ConfigTime.All, hour = ConfigTime.All, command = "/bin/run_me_every_minute"),
                ConfigLine(minutes = ConfigTime.All, hour = ConfigTime.Value(19), command = "/bin/run_me_sixty_times"),
            ),
        )
    }
}
