import utils.FileLoader
import java.io.FileNotFoundException

internal class FileDataSourceImpl(
    private val fileLoader: FileLoader,
) : FileDataSource {
    override fun loadFileFromPath(path: String): Result<List<String>> {
        val file = fileLoader.fileFromPath(path)
        if (!file.exists()) {
            return Result.failure(FileNotFoundException("We could not find a file at the given path."))
        }
        if (!file.canRead()) {
            return Result.failure(Exception("The provided file is not readable."))
        }
        return Result.success(fileLoader.linesFromFile(file))
    }
}
