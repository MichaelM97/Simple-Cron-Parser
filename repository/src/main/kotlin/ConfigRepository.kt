import models.ConfigLine

/**
 * Repository responsible for loading/saving config data.
 */
interface ConfigRepository {
    /**
     * Builds a list of validated [ConfigLine]'s.
     */
    fun getConfigLines(): Result<List<ConfigLine>>
}
