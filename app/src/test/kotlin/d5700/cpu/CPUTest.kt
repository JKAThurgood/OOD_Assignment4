package d5700.cpu

import d5700.memory.RAM
import d5700.memory.ROM
import d5700.strategy.RamStrategy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class CPUTest {

    @Test
    fun fetchInstructionReadsTwoBytesFromRom() {
        val rom = ROM(writable = true)
        rom.write(0, 0x10.toByte())
        rom.write(1, 0x00.toByte())

        val cpu = CPU().apply {
            setRom(rom)
            jumpTo(0)
        }

        assertEquals(0x1000.toShort(), cpu.fetchInstruction())
    }

    @Test
    fun incrementAndSkipAdjustProgramCounter() {
        val cpu = CPU().apply {
            jumpTo(4)
        }

        cpu.incrementPC()
        assertEquals(6, cpu.getProgramCounter())

        cpu.skipInstruction()
        assertEquals(10, cpu.getProgramCounter())
    }

    @Test
    fun jumpToRejectsOddProgramCounter() {
        val cpu = CPU()

        assertThrows(IllegalArgumentException::class.java) {
            cpu.jumpTo(3)
        }
    }

    @Test
    fun cycleExecutesInstructionFromRom() {
        val rom = ROM(writable = true)
        rom.write(0, 0x10.toByte())
        rom.write(1, 0x00.toByte())

        val ram = RAM()

        val cpu = CPU().apply {
            attachDevices(ram, rom, null, null)
            jumpTo(0)
        }

        cpu.cycle()

        assertEquals(2, cpu.getProgramCounter())
    }

    @Test
    fun terminateStopsExecution() {
        val cpu = CPU()

        val exception = assertThrows(IllegalStateException::class.java) {
            cpu.terminate("done")
        }

        assertEquals("done", exception.message)
        assertEquals(true, cpu.isHalted())
    }
}