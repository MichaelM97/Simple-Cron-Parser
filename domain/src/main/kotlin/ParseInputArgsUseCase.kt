import models.CurrentTime
import models.InputArgs

class ParseInputArgsUseCase internal constructor() {
    /**
     * Parses the passed [args] and returns [InputArgs] if valid.
     */
    operator fun invoke(args: Array<String>): Result<InputArgs> {
        if (args.size < 2) {
            return Result.failure(IllegalStateException("Not enough arguments passed, you must provide the config file path and current time."))
        }
        return try {
            val parsedTime = parseTimeArg(args[1])
            Result.success(
                InputArgs(
                    configFilePath = args.first(),
                    currentTime = parsedTime,
                ),
            )
        } catch (e: Exception) {
            Result.failure(IllegalArgumentException("The passed time argument is invalid."))
        }
    }

    @kotlin.jvm.Throws
    private fun parseTimeArg(timeArg: String): CurrentTime {
        val time = timeArg.split(TIME_SEPARATOR)
        if (time.size < 2) throw Exception()
        val hour = time[0].toInt()
        if (hour !in 0..23) throw Exception()
        val minute = time[1].toInt()
        if (minute !in 0..59) throw Exception()
        return CurrentTime(hour, minute)
    }

    private companion object {
        const val TIME_SEPARATOR = ":"
    }
}
