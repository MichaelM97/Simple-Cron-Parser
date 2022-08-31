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
    private val mockStandardInputDataSource: StandardInputDataSource = mockk()
    private val mockConfigTimeMapper: ConfigTimeMapper = mockk()
    private lateinit var configRepositoryImpl: ConfigRepositoryImpl

    @BeforeEach
    fun before() {
        every { mockConfigTimeMapper.fromString("*", any()) } returns Result.success(ConfigTime.All)
        configRepositoryImpl = ConfigRepositoryImpl(
            standardInputDataSource = mockStandardInputDataSource,
            configTimeMapper = mockConfigTimeMapper,
        )
    }

    @Nested
    inner class GetConfigLines {
        @Test
        fun `Should parse stdin lines and return success`() {
            // Given
            val stdinLines: List<String?> = listOf(
                "30 1 /bin/run_me_daily",
                "45 * /bin/run_me_hourly",
                "* * /bin/run_me_every_minute",
                "* 19 /bin/run_me_sixty_times",
                null,
            )
            every { mockStandardInputDataSource.readNextLine() } returnsMany stdinLines
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
            val result = configRepositoryImpl.getConfigLines()

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
        fun `Should parse stdin lines and return success, skipping invalid lines`() {
            // Given
            val stdinLines: List<String?> = listOf(
                "30 1 /bin/run_me_daily",
                "45 *",
                "/bin/run_me_hourly",
                "* * /bin/run_me_every_minute",
                "* 24 /bin/run_me_sixty_times",
                null,
            )
            every { mockStandardInputDataSource.readNextLine() } returnsMany stdinLines
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
            val result = configRepositoryImpl.getConfigLines()

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
        fun `Should return failure if no lines in stdin`() {
            // Given
            every { mockStandardInputDataSource.readNextLine() } returns null

            // When
            val result = configRepositoryImpl.getConfigLines()

            // Then
            assertTrue(result.isFailure)
        }

        @Test
        fun `Should return failure if all stdin lines are invalid`() {
            // Given
            val stdinLines: List<String?> = listOf(
                "45 *",
                "/bin/run_me_hourly",
                "* 24 /bin/run_me_sixty_times",
                "60 * /bin/run_me_sixty_times",
                null,
            )
            every { mockStandardInputDataSource.readNextLine() } returnsMany stdinLines
            every {
                mockConfigTimeMapper.fromString("24", ConfigTimeMapper.ConfigTimeType.HOUR)
            } returns Result.failure(Exception())
            every {
                mockConfigTimeMapper.fromString("60", ConfigTimeMapper.ConfigTimeType.MINUTE)
            } returns Result.failure(Exception())

            // When
            val result = configRepositoryImpl.getConfigLines()

            // Then
            assertTrue(result.isFailure)
        }
    }
}
