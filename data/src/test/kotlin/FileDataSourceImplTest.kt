import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import utils.FileLoader
import java.io.File

@ExtendWith(MockKExtension::class)
internal class FileDataSourceImplTest {
    private val mockFileLoader: FileLoader = mockk()
    private lateinit var fileDataSourceImpl: FileDataSourceImpl

    @BeforeEach
    fun before() {
        fileDataSourceImpl = FileDataSourceImpl(
            fileLoader = mockFileLoader,
        )
    }

    @Nested
    inner class LoadFileFromPath {
        @Test
        fun `Should return success when file valid`() {
            // Given
            val path = "my/path/file.txt"
            val mockFile: File = mockk()
            every { mockFile.exists() } returns true
            every { mockFile.canRead() } returns true
            every { mockFileLoader.fileFromPath(path) } returns mockFile
            val lines = listOf("line1", "line2", "line3")
            every { mockFileLoader.linesFromFile(mockFile) } returns lines

            // When
            val result = fileDataSourceImpl.loadFileFromPath(path)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(lines, result.getOrThrow())
        }

        @Test
        fun `Should fail when file does not exist`() {
            // Given
            val path = "my/path/file.txt"
            val mockFile: File = mockk()
            every { mockFile.exists() } returns false
            every { mockFile.canRead() } returns true
            every { mockFileLoader.fileFromPath(path) } returns mockFile

            // When
            val result = fileDataSourceImpl.loadFileFromPath(path)

            // Then
            assertTrue(result.isFailure)
        }

        @Test
        fun `Should fail when file is not readable`() {
            // Given
            val path = "my/path/file.txt"
            val mockFile: File = mockk()
            every { mockFile.exists() } returns true
            every { mockFile.canRead() } returns false
            every { mockFileLoader.fileFromPath(path) } returns mockFile

            // When
            val result = fileDataSourceImpl.loadFileFromPath(path)

            // Then
            assertTrue(result.isFailure)
        }
    }
}
