package d5700

import d5700.io.Display
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class D5700ComputerTest {
    @Test
    fun loadProgramLoadsProgramBytesIntoRom() {
        val computer = D5700Computer()
        val program = byteArrayOf(0x10.toByte(), 0x00.toByte(), 0x20.toByte(), 0x00.toByte())

        computer.loadProgram(program)

        assertEquals(0x10.toByte(), computer.rom.read(0))
        assertEquals(0x00.toByte(), computer.rom.read(1))
        assertEquals(0x20.toByte(), computer.rom.read(2))
    }

    @Test
    fun startAndStopManageSchedulers() {
        val computer = D5700Computer()

        computer.start()
        computer.stop()

        assertEquals(0, computer.cpu.getProgramCounter())
    }

    @Test
    fun constructorAllowsInjectedDisplay() {
        val display = FakeDisplay()
        val computer = D5700Computer(display = display)

        computer.cpu.setRegister(0, 0x7F.toByte())
        computer.reset()

        assertEquals(0, computer.cpu.getRegister(0).toInt())
        assertArrayEquals(ByteArray(64), display.render())
    }

    @Test
    fun resetClearsCpuAndScreenState() {
        val computer = D5700Computer()
        computer.cpu.setRegister(0, 0x7F.toByte())
        computer.cpu.setProgramCounter(4)
        computer.cpu.setTimer(9)
        computer.screen.draw(0x41.toByte(), 0, 0)

        computer.reset()

        assertEquals(0, computer.cpu.getProgramCounter())
        assertEquals(0, computer.cpu.getTimer().toInt())
        assertEquals(0, computer.cpu.getRegister(0).toInt())
        assertArrayEquals(ByteArray(64), computer.screen.render())
    }

    private class FakeDisplay : Display {
        private val buffer = ByteArray(64)

        override fun draw(ascii: Byte, row: Int, column: Int) {
            buffer[row * 8 + column] = ascii
        }

        override fun render(): ByteArray = buffer.copyOf()

        override fun clear() {
            buffer.fill(0)
        }
    }
}
