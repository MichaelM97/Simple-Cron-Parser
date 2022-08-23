import models.Config
import models.ConfigLine
import models.ConfigTime
import models.CurrentTime

class BuildConfigOutputUseCase internal constructor() {
    /**
     * Builds a list of output strings using the provided [config] and [currentTime].
     */
    operator fun invoke(config: Config, currentTime: CurrentTime): List<String> {
        val outputLines: MutableList<String> = mutableListOf()
        for (line in config.configLines) {
            val hour = line.buildHour(currentTime)
            val minute = line.buildMinute(currentTime, hour)
            val day: String = if (hour < currentTime.hour) TOMORROW else TODAY
            outputLines.add(line.buildOutputLine(hour, minute, day))
        }
        return outputLines
    }

    private fun ConfigLine.buildHour(currentTime: CurrentTime): Int {
        return when (val hour = hour) {
            ConfigTime.All -> {
                // If the specified minutes value has been elapsed in the current hour, increment to the next one
                val minute = minutes
                if (minute is ConfigTime.Value && minute.value < currentTime.minute) {
                    currentTime.nextHour
                } else {
                    currentTime.hour
                }
            }
            is ConfigTime.Value -> hour.value
        }
    }

    private fun ConfigLine.buildMinute(currentTime: CurrentTime, hour: Int): Int {
        return when (val minute = minutes) {
            ConfigTime.All -> {
                // In any hour other than the current one, the soonest minute is 00
                if (hour != currentTime.hour) 0 else currentTime.minute
            }
            is ConfigTime.Value -> minute.value
        }
    }

    private fun ConfigLine.buildOutputLine(hour: Int, minute: Int, day: String): String {
        val hourString = hour.toString().padStart(2, ZERO_CHAR)
        val minuteString = minute.toString().padStart(2, ZERO_CHAR)
        return "$hourString:$minuteString $day - $command"
    }

    private companion object {
        const val TODAY = "today"
        const val TOMORROW = "tomorrow"
        const val ZERO_CHAR = '0'
    }
}
