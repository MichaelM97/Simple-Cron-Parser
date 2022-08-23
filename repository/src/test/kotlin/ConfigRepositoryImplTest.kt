import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import mapper.ConfigTimeMapper
import models.ConfigLine
import models.ConfigTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ConfigRepositoryImplTest {
    private val mockFileDataSource: FileDataSource = mockk()
    private val mockConfigTimeMapper: ConfigTimeMapper = mockk()
    private lateinit var configRepositoryImpl: ConfigRepositoryImpl

    @BeforeEach
    fun before() {
        every { mockConfigTimeMapper.fromString("*", any()) } returns Result.success(ConfigTime.All)
        configRepositoryImpl = ConfigRepositoryImpl(
            fileDataSource = mockFileDataSource,
            configTimeMapper = mockConfigTimeMapper,
        )
    }

    @Nested
    inner class LoadConfigFile {
        @Test
        fun `Should parse config file and return success`() {
            // Given
            val path = "my/path/config.txt"
            val fileLines = listOf(
                "30 1 /bin/run_me_daily",
                "45 * /bin/run_me_hourly",
                "* * /bin/run_me_every_minute",
                "* 19 /bin/run_me_sixty_times",
            )
            every { mockFileDataSource.loadFileFromPath(path) } returns Result.success(fileLines)
            every {
                mockConfigTimeMapper.fromString("30", ConfigTimeMapper.ConfigTimeType.MINUTE)
            } returns Result.success(ConfigTime.Value(30))
            every {
                mockConfigTimeMapper.fromString("1", ConfigTimeMapper.ConfigTimeType.HOUR)
            } returns Result.success(ConfigTime.Value(1))
            every {
                mockConfigTimeMapper.fromString("45", ConfigTimeMapper.ConfigTimeType.MINUTE)
            } returns Result.success(ConfigTime.Value(45))
            every {
                mockConfigTimeMapper.fromString("19", ConfigTimeMapper.ConfigTimeType.HOUR)
            } returns Result.success(ConfigTime.Value(19))

            // When
            val result = configRepositoryImpl.loadConfigFile(path)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(
                listOf(
                    ConfigLine(
                        minutes = ConfigTime.Value(30),
                        hour = ConfigTime.Value(1),
                        command = "/bin/run_me_daily",
                    ),
                    ConfigLine(
                        minutes = ConfigTime.Value(45),
                        hour = ConfigTime.All,
                        command = "/bin/run_me_hourly",
                    ),
                    ConfigLine(
                        minutes = ConfigTime.All,
                        hour = ConfigTime.All,
                        command = "/bin/run_me_every_minute",
                    ),
                    ConfigLine(
                        minutes = ConfigTime.All,
                        hour = ConfigTime.Value(19),
                        command = "/bin/run_me_sixty_times",
                    ),
                ),
                result.getOrThrow(),
            )
        }

        @Test
        fun `Should parse config file and return success, skipping invalid lines`() {
            // Given
            val path = "my/path/config.txt"
            val fileLines = listOf(
                "30 1 /bin/run_me_daily",
                "45 *",
                "/bin/run_me_hourly",
                "* * /bin/run_me_every_minute",
                "* 24 /bin/run_me_sixty_times",
            )
            every { mockFileDataSource.loadFileFromPath(path) } returns Result.success(fileLines)
            every {
                mockConfigTimeMapper.fromString("30", ConfigTimeMapper.ConfigTimeType.MINUTE)
            } returns Result.success(ConfigTime.Value(30))
            every {
                mockConfigTimeMapper.fromString("1", ConfigTimeMapper.ConfigTimeType.HOUR)
            } returns Result.success(ConfigTime.Value(1))
            every {
                mockConfigTimeMapper.fromString("24", ConfigTimeMapper.ConfigTimeType.HOUR)
            } returns Result.failure(Exception())

            // When
            val result = configRepositoryImpl.loadConfigFile(path)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(
                listOf(
                    ConfigLine(
                        minutes = ConfigTime.Value(30),
                        hour = ConfigTime.Value(1),
                        command = "/bin/run_me_daily",
                    ),
                    ConfigLine(
                        minutes = ConfigTime.All,
                        hour = ConfigTime.All,
                        command = "/bin/run_me_every_minute",
                    ),
                ),
                result.getOrThrow(),
            )
        }

        @Test
        fun `Should return failure if loading file failed`() {
            // Given
            val path = "my/path/config.txt"
            every { mockFileDataSource.loadFileFromPath(path) } returns Result.failure(Exception())

            // When
            val result = configRepositoryImpl.loadConfigFile(path)

            // Then
            assertTrue(result.isFailure)
        }

        @Test
        fun `Should return failure if all lines are invalid`() {
            // Given
            val path = "my/path/config.txt"
            val fileLines = listOf(
                "45 *",
                "/bin/run_me_hourly",
                "* 24 /bin/run_me_sixty_times",
                "60 * /bin/run_me_sixty_times",
            )
            every { mockFileDataSource.loadFileFromPath(path) } returns Result.success(fileLines)
            every {
                mockConfigTimeMapper.fromString("24", ConfigTimeMapper.ConfigTimeType.HOUR)
            } returns Result.failure(Exception())
            every {
                mockConfigTimeMapper.fromString("60", ConfigTimeMapper.ConfigTimeType.MINUTE)
            } returns Result.failure(Exception())

            // When
            val result = configRepositoryImpl.loadConfigFile(path)

            // Then
            assertTrue(result.isFailure)
        }
    }
}
