package d5700.cpu

import d5700.instruction.DrawInstruction
import d5700.instruction.ReadKeyboardInstruction
import d5700.io.Display
import d5700.io.InputDevice
import d5700.memory.RAM
import d5700.memory.ROM
import d5700.strategy.RamStrategy
import d5700.strategy.RomStrategy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CPUDependenciesTest {
    @Test
    fun switchMemoryTogglesBetweenRamAndRomStrategies() {
        val cpu = CPU().apply {
            attachDevices(RAM(), ROM(writable = true), null, null)
            setMemoryStrategy(RamStrategy(getRam()!!))
        }

        cpu.switchMemory()
        assertTrue(cpu.getMemoryStrategy() is RomStrategy)

        cpu.switchMemory()
        assertTrue(cpu.getMemoryStrategy() is RamStrategy)
    }

    @Test
    fun drawInstructionUsesInjectedDisplay() {
        val fakeDisplay = FakeDisplay()
        val cpu = CPU().apply {
            setDisplay(fakeDisplay)
            setMemoryStrategy(RamStrategy(RAM()))
            setRegister(0, 0x41.toByte())
            setRegister(1, 0x00.toByte())
            setRegister(2, 0x00.toByte())
        }

        DrawInstruction().execute(cpu)

        assertEquals(0x41.toByte(), fakeDisplay.lastAscii)
    }

    @Test
    fun readKeyboardInstructionUsesInjectedInput() {
        val fakeInput = FakeInputDevice(0x5A.toByte())
        val cpu = CPU().apply {
            setInput(fakeInput)
            setMemoryStrategy(RamStrategy(RAM()))
        }

        ReadKeyboardInstruction().execute(cpu)

        assertEquals(0x5A.toByte(), cpu.getRegister(0))
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
