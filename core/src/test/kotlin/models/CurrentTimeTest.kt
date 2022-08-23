package models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class CurrentTimeTest {
    @ParameterizedTest
    @CsvSource(value = ["23, 0", "0, 1", "22, 23", "12, 13"])
    fun `Should calculate correct nextHour`(hour: Int, expectedNextHour: Int) {
        // Given
        val currentTime = CurrentTime(hour = hour, minute = 0)

        // Then
        assertEquals(expectedNextHour, currentTime.nextHour)
    }
}
