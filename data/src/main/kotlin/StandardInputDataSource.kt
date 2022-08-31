/**
 * Data source responsible for interacting with stdin.
 */
interface StandardInputDataSource {
    /**
     * Reads the next line from stdin.
     * @return the next line or null if no lines remain.
     */
    fun readNextLine(): String?
}
