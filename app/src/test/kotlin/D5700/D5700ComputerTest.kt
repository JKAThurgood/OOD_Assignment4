package D5700

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

        assertEquals(0, computer.cpu.pc)
    }

    @Test
    fun resetClearsCpuAndScreenState() {
        val computer = D5700Computer()
        computer.cpu.registers[0] = 0x7F.toByte()
        computer.cpu.pc = 4
        computer.cpu.timer = 9
        computer.screen.draw(0x41.toByte(), 0, 0)

        computer.reset()

        assertEquals(0, computer.cpu.pc)
        assertEquals(0, computer.cpu.timer.toInt())
        assertEquals(0, computer.cpu.registers[0].toInt())
        assertArrayEquals(ByteArray(64), computer.screen.render())
    }
}
