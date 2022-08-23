import java.io.File

/**
 * Data source responsible for interacting with files.
 */
interface FileDataSource {
    /**
     * Loads and validates the [File] at the given [path], returning the lines of the [File].
     */
    fun loadFileFromPath(path: String): Result<List<String>>
}
