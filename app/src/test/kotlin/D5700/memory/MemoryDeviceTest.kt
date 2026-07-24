package D5700.memory

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

class MemoryDeviceTest {
    @Test
    fun ramSupportsReadsAndWrites() {
        val ram = RAM()

        ram.write(0, 7)
        ram.write(4095, -1)

        assertEquals(7, ram.read(0))
        assertEquals(-1, ram.read(4095))
    }

    @Test
    fun romReadsAndRejectsWritesWhenReadOnly() {
        val rom = ROM()

        assertEquals(0, rom.read(0))
        assertThrows(IllegalStateException::class.java, Executable {
            rom.write(0, 1)
        })
    }

    @Test
    fun writableRomAllowsWrites() {
        val rom = ROM(writable = true)

        rom.write(10, 42)

        assertEquals(42, rom.read(10))
    }

    @Test
    fun memoryRejectsOutOfRangeAddresses() {
        val ram = RAM()
        val rom = ROM()

        assertThrows(IllegalArgumentException::class.java, Executable {
            ram.read(-1)
        })
        assertThrows(IllegalArgumentException::class.java, Executable {
            ram.write(4096, 1)
        })
        assertThrows(IllegalArgumentException::class.java, Executable {
            rom.read(5000)
        })
    }
}
