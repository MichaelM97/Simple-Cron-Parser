package utils

import java.io.File

/**
 * Helper class for [File] interactions.
 */
internal class FileLoader {
    /**
     * Creates an instance of [File] using the given [path].
     */
    fun fileFromPath(path: String): File = File(path)

    /**
     * Returns the lines from the given [file].
     */
    fun linesFromFile(file: File): List<String> {
        val lines: MutableList<String> = mutableListOf()
        file.forEachLine { lines.add(it) }
        return lines
    }
}
