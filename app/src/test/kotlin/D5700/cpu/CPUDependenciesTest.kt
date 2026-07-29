package D5700.cpu

import D5700.instruction.DrawInstruction
import D5700.instruction.ReadKeyboardInstruction
import D5700.io.Display
import D5700.io.InputDevice
import D5700.memory.RAM
import D5700.memory.ROM
import D5700.strategy.RamStrategy
import D5700.strategy.RomStrategy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CPUDependenciesTest {
    @Test
    fun switchMemoryTogglesBetweenRamAndRomStrategies() {
        val cpu = CPU().apply {
            ram = RAM()
            rom = ROM(writable = true)
            memoryStrategy = RamStrategy(ram!!)
        }

        cpu.switchMemory()
        assertTrue(cpu.memoryStrategy is RomStrategy)

        cpu.switchMemory()
        assertTrue(cpu.memoryStrategy is RamStrategy)
    }

    @Test
    fun drawInstructionUsesInjectedDisplay() {
        val fakeDisplay = FakeDisplay()
        val cpu = CPU().apply {
            display = fakeDisplay
            memoryStrategy = RamStrategy(RAM())
            registers[0] = 0x41.toByte()
            registers[1] = 0x00.toByte()
            registers[2] = 0x00.toByte()
        }

        DrawInstruction().execute(cpu)

        assertEquals(0x41.toByte(), fakeDisplay.lastAscii)
    }

    @Test
    fun readKeyboardInstructionUsesInjectedInput() {
        val fakeInput = FakeInputDevice(0x5A.toByte())
        val cpu = CPU().apply {
            input = fakeInput
            memoryStrategy = RamStrategy(RAM())
        }

        ReadKeyboardInstruction().execute(cpu)

        assertEquals(0x5A.toByte(), cpu.registers[0])
    }

    private class FakeDisplay : Display {
        var lastAscii: Byte? = null
        var lastRow: Int? = null
        var lastColumn: Int? = null

        override fun draw(ascii: Byte, row: Int, column: Int) {
            lastAscii = ascii
            lastRow = row
            lastColumn = column
        }

        override fun render(): ByteArray = ByteArray(64)
        override fun clear() = Unit
    }

    private class FakeInputDevice(private val value: Byte) : InputDevice {
        override fun readHexByte(): Byte = value
    }
}
