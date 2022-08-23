package mapper

import models.ConfigTime

/**
 * Validates & maps inputs to a [ConfigTime].
 */
internal class ConfigTimeMapper {
    /**
     * Maps the passed [string] to a [ConfigTime] if valid. The [string] is validated using the specific [type].
     */
    fun fromString(string: String, type: ConfigTimeType): Result<ConfigTime> {
        with(string.strip()) {
            if (this == ALL_TIME_CHAR) {
                return Result.success(ConfigTime.All)
            }
            return try {
                val time = this.toInt()
                when (type) {
                    ConfigTimeType.MINUTE -> if (time !in 0..59) return INVALID_TIME_FAILURE
                    ConfigTimeType.HOUR -> if (time !in 0..23) return INVALID_TIME_FAILURE
                }
                Result.success(ConfigTime.Value(time))
            } catch (e: NumberFormatException) {
                Result.failure(e)
            }
        }
    }

    enum class ConfigTimeType { MINUTE, HOUR }

    private companion object {
        const val ALL_TIME_CHAR = "*"
        val INVALID_TIME_FAILURE = Result.failure<ConfigTime>(Exception("Invalid time."))
    }
}
