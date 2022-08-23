package di

import utils.FileLoader

internal class FileLoaderFactory : Factory<FileLoader> {
    override fun create(): FileLoader {
        return FileLoader()
    }
}
