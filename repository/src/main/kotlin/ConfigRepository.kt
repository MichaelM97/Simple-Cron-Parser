import models.ConfigLine

/**
 * Repository responsible for loading/saving config data.
 */
interface ConfigRepository {
    /**
     * Loads & reads the config located at the passed [configFilePath].
     * Returns a list of validated [ConfigLine]'s.
     */
    fun loadConfigFile(configFilePath: String): Result<List<ConfigLine>>
}
