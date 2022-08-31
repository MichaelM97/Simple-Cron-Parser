internal class StandardInputDataSourceImpl : StandardInputDataSource {
    override fun readNextLine(): String? {
        return readlnOrNull()
    }
}
