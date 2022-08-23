import mapper.ConfigTimeMapper
import models.ConfigLine

internal class ConfigRepositoryImpl(
    private val fileDataSource: FileDataSource,
    private val configTimeMapper: ConfigTimeMapper,
) : ConfigRepository {
    override fun loadConfigFile(configFilePath: String): Result<List<ConfigLine>> {
        val fileLines = fileDataSource.loadFileFromPath(configFilePath)
        return if (fileLines.isSuccess) {
            val configLines = parseConfigFileLines(fileLines.getOrThrow())
            if (configLines.isNotEmpty()) {
                Result.success(configLines)
            } else {
                Result.failure(Exception("No valid entries were found in the config file."))
            }
        } else {
            Result.failure(fileLines.exceptionOrNull() ?: Exception())
        }
    }

    private fun parseConfigFileLines(fileLines: List<String>): List<ConfigLine> {
        val configLines: MutableList<ConfigLine> = mutableListOf()
        fileLines.forEach {
            with(it.split(" ")) {
                // If line has less than 3 args, skip it
                if (size < 3) return@forEach
                // Validate minute & hour args, skip line if invalid
                val minute = configTimeMapper.fromString(this[0], ConfigTimeMapper.ConfigTimeType.MINUTE)
                val hour = configTimeMapper.fromString(this[1], ConfigTimeMapper.ConfigTimeType.HOUR)
                if (minute.isFailure || hour.isFailure) return@forEach
                configLines.add(
                    ConfigLine(
                        minutes = minute.getOrThrow(),
                        hour = hour.getOrThrow(),
                        command = this[2],
                    ),
                )
            }
        }
        return configLines
    }
}
