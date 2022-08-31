import mapper.ConfigTimeMapper
import models.ConfigLine

internal class ConfigRepositoryImpl(
    private val standardInputDataSource: StandardInputDataSource,
    private val configTimeMapper: ConfigTimeMapper,
) : ConfigRepository {
    override fun getConfigLines(): Result<List<ConfigLine>> {
        val configLines: MutableList<ConfigLine> = mutableListOf()
        var currentLine: String? = standardInputDataSource.readNextLine()
        while (currentLine != null) {
            parseLine(currentLine)?.let { configLines.add(it) }
            currentLine = standardInputDataSource.readNextLine()
        }
        return if (configLines.isNotEmpty()) {
            Result.success(configLines)
        } else {
            Result.failure(Exception("No valid config entries were found in stdin."))
        }
    }

    private fun parseLine(line: String): ConfigLine? {
        with(line.split(" ")) {
            // If line has less than 3 args it's invalid
            if (size < 3) return null
            // Validate minute & hour args
            val minute = configTimeMapper.fromString(this[0], ConfigTimeMapper.ConfigTimeType.MINUTE)
            val hour = configTimeMapper.fromString(this[1], ConfigTimeMapper.ConfigTimeType.HOUR)
            if (minute.isFailure || hour.isFailure) return null
            // Return valid ConfigLine
            return ConfigLine(
                minutes = minute.getOrThrow(),
                hour = hour.getOrThrow(),
                command = this[2],
            )
        }
    }
}
