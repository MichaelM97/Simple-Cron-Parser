package di

/**
 * Factory for creating instances of [T].
 */
interface Factory<T> {
    /**
     * Creates and returns an instance of [T].
     */
    fun create(): T
}
