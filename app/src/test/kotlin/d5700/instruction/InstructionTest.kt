package d5700.instruction

import d5700.cpu.CPU
import d5700.factory.InstructionFactory
import d5700.memory.RAM
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InstructionTest {

    private fun cpuWithRam(): CPU {
        return CPU().apply {
            attachDevices(RAM(), null, null, null)
        }
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