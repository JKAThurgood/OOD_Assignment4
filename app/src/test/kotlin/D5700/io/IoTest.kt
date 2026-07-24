package D5700.io

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class IoTest {
    @Test
    fun screenStoresBytesInTheExpectedPosition() {
        val screen = Screen.instance()

        screen.draw(65, 0, 0)
        screen.draw(66, 7, 7)

        val rendered = screen.render()
        assertEquals(65, rendered[0].toInt())
        assertEquals(66, rendered[63].toInt())
    }

    @Test
    fun screenRejectsOutOfRangeCoordinates() {
        val screen = Screen.instance()

        assertThrows(IllegalArgumentException::class.java, Executable {
            screen.draw(65, -1, 0)
        })
        assertThrows(IllegalArgumentException::class.java, Executable {
            screen.draw(65, 0, 8)
        })
    }

    @Test
    fun keyboardReturnsZeroForEmptyInput() {
        val keyboard = Keyboard()
        val originalIn = System.`in`
        val originalOut = System.out
        try {
            System.setIn(ByteArrayInputStream("\n".toByteArray()))
            System.setOut(PrintStream(ByteArrayOutputStream()))
            assertEquals(0, keyboard.readHexByte().toInt())
        } finally {
            System.setIn(originalIn)
            System.setOut(originalOut)
        }
    }

    @Test
    fun keyboardParsesHexBytesAndRejectsInvalidInput() {
        val keyboard = Keyboard()
        val originalIn = System.`in`
        val originalOut = System.out
        try {
            System.setIn(ByteArrayInputStream("ff\n".toByteArray()))
            System.setOut(PrintStream(ByteArrayOutputStream()))
            assertEquals(-1, keyboard.readHexByte().toInt())

            System.setIn(ByteArrayInputStream("100\n".toByteArray()))
            assertThrows(IllegalArgumentException::class.java, Executable {
                keyboard.readHexByte()
            })

            System.setIn(ByteArrayInputStream("g\n".toByteArray()))
            assertThrows(IllegalArgumentException::class.java, Executable {
                keyboard.readHexByte()
            })
        } finally {
            System.setIn(originalIn)
            System.setOut(originalOut)
        }
    }
}
