package d5700.cpu

import d5700.instruction.DrawInstruction
import d5700.instruction.ReadKeyboardInstruction
import d5700.io.Display
import d5700.io.InputDevice
import d5700.memory.RAM
import d5700.memory.ROM
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CPUDependenciesTest {

    @Test
    fun switchMemoryTogglesBetweenRamAndRomStrategies() {
        val cpu = CPU().apply {
            attachDevices(
                RAM(),
                ROM(writable = true),
                null,
                null
            )
        }

        cpu.switchMemory()
        assertTrue(cpu.hasMemory())

        cpu.switchMemory()
        assertTrue(cpu.hasMemory())
    }

    @Test
    fun drawInstructionUsesInjectedDisplay() {
        val fakeDisplay = FakeDisplay()

        val cpu = CPU().apply {
            attachDevices(
                RAM(),
                null,
                fakeDisplay,
                null
            )

            writeRegister(0, 0x41.toByte())
            writeRegister(1, 0)
            writeRegister(2, 0)
        }

        DrawInstruction().execute(cpu)

        assertEquals(0x41.toByte(), fakeDisplay.lastAscii)
    }

    @Test
    fun readKeyboardInstructionUsesInjectedInput() {
        val fakeInput = FakeInputDevice(0x5A.toByte())

        val cpu = CPU().apply {
            attachDevices(
                RAM(),
                null,
                null,
                fakeInput
            )
        }

        ReadKeyboardInstruction().execute(cpu)

        assertEquals(
            0x5A.toByte(),
            cpu.readRegister(0)
        )
    }

    private class FakeDisplay : Display {
        var lastAscii: Byte? = null

        override fun draw(ascii: Byte, row: Int, column: Int) {
            lastAscii = ascii
        }

        override fun render(): ByteArray = ByteArray(64)

        override fun clear() = Unit
    }

    private class FakeInputDevice(
        private val value: Byte
    ) : InputDevice {
        override fun readHexByte(): Byte = value
    }
}