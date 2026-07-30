package d5700.instruction

import d5700.cpu.CPU
import d5700.factory.InstructionFactory
import d5700.hardware.Hardware
import d5700.io.Keyboard
import d5700.io.Screen
import d5700.memory.RAM
import d5700.memory.ROM
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InstructionTest {

    private fun cpuWithRam(): CPU {
        val ram = RAM()
        val rom = ROM(writable = true)

        return CPU(
            Hardware(
                ram = ram,
                rom = rom,
                display = Screen(),
                input = Keyboard()
            )
        )
    }

    @Test
    fun normalInstructionsAdvanceProgramCounterByTwo() {
        val cpu = cpuWithRam().apply {
            jumpTo(0)
        }

        AddInstruction().execute(cpu)

        assertEquals(2, cpu.getProgramCounter())
    }

    @Test
    fun jumpInstructionDoesNotAdvanceProgramCounter() {
        val cpu = cpuWithRam().apply {
            jumpTo(10)
        }

        InstructionFactory()
            .create(0x501E)
            .execute(cpu)

        assertEquals(30, cpu.getProgramCounter())
    }

    @Test
    fun skipInstructionUsesFourStepIncrement() {
        val cpu = cpuWithRam().apply {
            jumpTo(4)
            writeRegister(0, 1)
            writeRegister(1, 1)
        }

        SkipEqualInstruction().execute(cpu)

        assertEquals(8, cpu.getProgramCounter())
    }
}