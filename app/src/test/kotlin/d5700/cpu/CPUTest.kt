package d5700.cpu

import d5700.hardware.Hardware
import d5700.io.Keyboard
import d5700.io.Screen
import d5700.memory.RAM
import d5700.memory.ROM
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class CPUTest {

    @Test
    fun fetchInstructionReadsTwoBytesFromRom() {
        val rom = ROM(writable = true)
        rom.write(0, 0x10.toByte())
        rom.write(1, 0x00.toByte())

        val cpu = CPU(
            Hardware(
                ram = RAM(),
                rom = rom,
                display = Screen(),
                input = Keyboard()
            )
        ).apply {
            jumpTo(0)
        }

        assertEquals(0x1000.toShort(), cpu.fetchInstruction())
    }

    @Test
    fun incrementAndSkipAdjustProgramCounter() {
        val cpu = CPU(
            Hardware(
                ram = RAM(),
                rom = ROM(writable = true),
                display = Screen(),
                input = Keyboard()
            )
        ).apply {
            jumpTo(4)
        }

        cpu.incrementPC()
        assertEquals(6, cpu.getProgramCounter())

        cpu.skipInstruction()
        assertEquals(10, cpu.getProgramCounter())
    }

    @Test
    fun jumpToRejectsOddProgramCounter() {
        val cpu = CPU(
            Hardware(
                ram = RAM(),
                rom = ROM(writable = true),
                display = Screen(),
                input = Keyboard()
            )
        )

        assertThrows(IllegalArgumentException::class.java) {
            cpu.jumpTo(3)
        }
    }

    @Test
    fun cycleExecutesInstructionFromRom() {
        val rom = ROM(writable = true)
        rom.write(0, 0x10.toByte())
        rom.write(1, 0x00.toByte())

        val cpu = CPU(
            Hardware(
                ram = RAM(),
                rom = rom,
                display = Screen(),
                input = Keyboard()
            )
        ).apply {
            jumpTo(0)
        }

        cpu.cycle()

        assertEquals(2, cpu.getProgramCounter())
    }

    @Test
    fun terminateStopsExecution() {
        val cpu = CPU(
            Hardware(
                ram = RAM(),
                rom = ROM(writable = true),
                display = Screen(),
                input = Keyboard()
            )
        )

        val exception = assertThrows(IllegalStateException::class.java) {
            cpu.terminate("done")
        }

        assertEquals("done", exception.message)
        assertEquals(true, cpu.isHalted())
    }
}