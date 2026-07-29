package d5700.cpu

import d5700.memory.RAM
import d5700.memory.ROM
import d5700.strategy.RamStrategy
import d5700.strategy.RomStrategy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

class CPUTest {
    @Test
    fun fetchInstructionReadsTwoBytesFromRom() {
        val rom = ROM(writable = true)
        rom.write(0, 0x10.toByte())
        rom.write(1, 0x00.toByte())

        val cpu = CPU().apply {
            this.rom = rom
            pc = 0
        }

        assertEquals(0x1000.toShort(), cpu.fetchInstruction())
    }

    @Test
    fun incrementAndSkipAdjustProgramCounter() {
        val cpu = CPU().apply { pc = 4 }

        cpu.incrementPC()
        assertEquals(6, cpu.pc)

        cpu.skipInstruction()
        assertEquals(10, cpu.pc)
    }

    @Test
    fun cycleExecutesInstructionFromRom() {
        val rom = ROM(writable = true)
        rom.write(0, 0x10.toByte())
        rom.write(1, 0x00.toByte())

        val cpu = CPU().apply {
            this.rom = rom
            pc = 0
            memoryStrategy = RamStrategy(RAM())
        }

        cpu.cycle()

        assertEquals(2, cpu.pc)
    }

    @Test
    fun terminateStopsExecution() {
        val cpu = CPU()

        val exception = assertThrows(IllegalStateException::class.java, Executable {
            cpu.terminate("done")
        })

        assertEquals("done", exception.message)
    }
}
