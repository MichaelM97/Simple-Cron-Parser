import models.Config

class LoadConfigUseCase internal constructor(
    private val configRepository: ConfigRepository,
) {
    /**
     * Loads the config file located at the passed [configFilePath].
     */
    operator fun invoke(configFilePath: String): Result<Config> {
        val configLines = configRepository.loadConfigFile(configFilePath)
        return if (configLines.isSuccess) {
            Result.success(Config(configLines = configLines.getOrThrow()))
        } else {
            Result.failure(configLines.exceptionOrNull() ?: Exception())
        }
    }
}
