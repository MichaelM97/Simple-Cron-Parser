package di

class DataModule {
    private val fileLoaderFactory: FileLoaderFactory = FileLoaderFactory()
    val fileDataSourceFactory: FileDataSourceFactory = FileDataSourceFactory(
        fileLoader = fileLoaderFactory.create(),
    )
}
