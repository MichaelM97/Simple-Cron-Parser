import models.Config

class BuildConfigUseCase internal constructor(
    private val configRepository: ConfigRepository,
) {
    /**
     * Loads a list of config lines from stdin & builds a [Config].
     */
    operator fun invoke(): Result<Config> {
        val configLines = configRepository.getConfigLines()
        return if (configLines.isSuccess) {
            Result.success(Config(configLines = configLines.getOrThrow()))
        } else {
            Result.failure(configLines.exceptionOrNull() ?: Exception())
        }
    }
}
