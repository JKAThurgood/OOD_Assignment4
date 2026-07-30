package d5700.instruction

import d5700.cpu.CPU
import d5700.factory.InstructionFactory
import d5700.memory.RAM
import d5700.strategy.RamStrategy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InstructionTest {
    @Test
    fun normalInstructionsAdvanceProgramCounterByTwo() {
        val cpu = CPU().apply {
            setProgramCounter(0)
            setMemoryStrategy(RamStrategy(RAM()))
        }

        AddInstruction().execute(cpu)

        assertEquals(2, cpu.getProgramCounter())
    }

    @Test
    fun jumpInstructionDoesNotAdvanceProgramCounter() {
        val cpu = CPU().apply {
            setProgramCounter(10)
            setMemoryStrategy(RamStrategy(RAM()))
        }

        InstructionFactory()
            .create(0x501E)
            .execute(cpu)

        assertEquals(30, cpu.getProgramCounter())
    }

    @Test
    fun skipInstructionUsesFourStepIncrement() {
        val cpu = CPU().apply {
            setProgramCounter(4)
            setRegister(0, 1)
            setRegister(1, 1)
            setMemoryStrategy(RamStrategy(RAM()))
        }

        SkipEqualInstruction().execute(cpu)

        assertEquals(8, cpu.getProgramCounter())
    }
}
