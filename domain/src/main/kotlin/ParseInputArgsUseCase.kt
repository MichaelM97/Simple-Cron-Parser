import models.CurrentTime
import models.InputArgs

class ParseInputArgsUseCase internal constructor() {
    /**
     * Parses the passed [args] and returns the validated [InputArgs].
     */
    operator fun invoke(args: Array<String>): Result<InputArgs> {
        if (args.isEmpty()) {
            return Result.failure(IllegalStateException("Not enough arguments passed, you must provide the current time."))
        }
        return try {
            val parsedTime = parseTimeArg(args.first())
            Result.success(InputArgs(currentTime = parsedTime))
        } catch (e: Exception) {
            Result.failure(IllegalArgumentException("The passed time argument is invalid."))
        }
    }

    @kotlin.jvm.Throws
    private fun parseTimeArg(timeArg: String): CurrentTime {
        val time = timeArg.split(TIME_SEPARATOR)
        // If there are not values on both sides of the semicolon, time is invalid
        if (time.size < 2) throw Exception()
        // If hour is not in the range 0-23, time is invalid
        val hour = time[0].toInt()
        if (hour !in 0..23) throw Exception()
        // If minutes are not in the range 0-59, time is invalid
        val minute = time[1].toInt()
        if (minute !in 0..59) throw Exception()
        // Return valid time
        return CurrentTime(hour, minute)
    }

    private companion object {
        const val TIME_SEPARATOR = ":"
    }
}
