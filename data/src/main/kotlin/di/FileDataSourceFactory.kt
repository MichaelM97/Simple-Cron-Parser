package di

import FileDataSource
import FileDataSourceImpl
import utils.FileLoader

class FileDataSourceFactory internal constructor(
    private val fileLoader: FileLoader,
) : Factory<FileDataSource> {
    override fun create(): FileDataSource {
        return FileDataSourceImpl(
            fileLoader = fileLoader,
        )
    }
}
