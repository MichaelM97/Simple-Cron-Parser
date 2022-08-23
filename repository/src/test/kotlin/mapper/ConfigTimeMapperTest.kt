package mapper

import models.ConfigTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ConfigTimeMapperTest {
    private lateinit var configTimeMapper: ConfigTimeMapper

    @BeforeEach
    fun before() {
        configTimeMapper = ConfigTimeMapper()
    }

    @Nested
    inner class FromString {
        @Test
        fun `Should parse valid hour`() {
            // Given
            val timeString = "13"

            // When
            val result = configTimeMapper.fromString(timeString, ConfigTimeMapper.ConfigTimeType.HOUR)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(ConfigTime.Value(13), result.getOrThrow())
        }

        @Test
        fun `Should parse valid minute`() {
            // Given
            val timeString = "45"

            // When
            val result = configTimeMapper.fromString(timeString, ConfigTimeMapper.ConfigTimeType.MINUTE)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(ConfigTime.Value(45), result.getOrThrow())
        }

        @Test
        fun `Should parse asterisk hour`() {
            // Given
            val timeString = "*"

            // When
            val result = configTimeMapper.fromString(timeString, ConfigTimeMapper.ConfigTimeType.HOUR)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(ConfigTime.All, result.getOrThrow())
        }

        @Test
        fun `Should parse asterisk minute`() {
            // Given
            val timeString = "    *     "

            // When
            val result = configTimeMapper.fromString(timeString, ConfigTimeMapper.ConfigTimeType.MINUTE)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(ConfigTime.All, result.getOrThrow())
        }

        @Test
        fun `Should fail when negative hour`() {
            // Given
            val timeString = "-1"

            // When
            val result = configTimeMapper.fromString(timeString, ConfigTimeMapper.ConfigTimeType.HOUR)

            // Then
            assertTrue(result.isFailure)
        }

        @Test
        fun `Should fail when negative minute`() {
            // Given
            val timeString = "-1"

            // When
            val result = configTimeMapper.fromString(timeString, ConfigTimeMapper.ConfigTimeType.MINUTE)

            // Then
            assertTrue(result.isFailure)
        }

        @Test
        fun `Should fail when hour too large`() {
            // Given
            val timeString = "24"

            // When
            val result = configTimeMapper.fromString(timeString, ConfigTimeMapper.ConfigTimeType.HOUR)

            // Then
            assertTrue(result.isFailure)
        }

        @Test
        fun `Should fail when minute too large`() {
            // Given
            val timeString = "60"

            // When
            val result = configTimeMapper.fromString(timeString, ConfigTimeMapper.ConfigTimeType.HOUR)

            // Then
            assertTrue(result.isFailure)
        }
    }
}
