package d5700.instruction

import d5700.cpu.CPU
import d5700.memory.RAM
import d5700.strategy.RamStrategy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InstructionTest {
    @Test
    fun normalInstructionsAdvanceProgramCounterByTwo() {
        val cpu = CPU().apply {
            pc = 10
            memoryStrategy = RamStrategy(RAM())
        }

        AddInstruction().execute(cpu)

        assertEquals(12, cpu.pc)
    }

    @Test
    fun jumpInstructionDoesNotAdvanceProgramCounter() {
        val cpu = CPU().apply {
            pc = 10
            address = 30
            memoryStrategy = RamStrategy(RAM())
        }

        JumpInstruction().execute(cpu)

        assertEquals(30, cpu.pc)
    }

    @Test
    fun skipInstructionUsesFourStepIncrement() {
        val cpu = CPU().apply {
            pc = 4
            registers[0] = 1
            registers[1] = 1
            memoryStrategy = RamStrategy(RAM())
        }

        SkipEqualInstruction().execute(cpu)

        assertEquals(8, cpu.pc)
    }
}
