package D5700.factory

import D5700.instruction.AddInstruction
import D5700.instruction.ConvertAsciiInstruction
import D5700.instruction.ConvertBase10Instruction
import D5700.instruction.DrawInstruction
import D5700.instruction.HaltInstruction
import D5700.instruction.JumpInstruction
import D5700.instruction.ReadInstruction
import D5700.instruction.ReadKeyboardInstruction
import D5700.instruction.ReadTInstruction
import D5700.instruction.SetAInstruction
import D5700.instruction.SetTInstruction
import D5700.instruction.SkipEqualInstruction
import D5700.instruction.SkipNotEqualInstruction
import D5700.instruction.SubInstruction
import D5700.instruction.SwitchMemoryInstruction
import D5700.instruction.WriteInstruction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

class InstructionFactoryTest {
    private val factory = InstructionFactory()

    @Test
    fun createsTheCorrectInstructionForEachOpcode() {
        assertEquals(HaltInstruction::class, factory.create(0x0000.toShort())::class)
        assertEquals(AddInstruction::class, factory.create(0x1000.toShort())::class)
        assertEquals(SubInstruction::class, factory.create(0x2000.toShort())::class)
        assertEquals(ReadInstruction::class, factory.create(0x3000.toShort())::class)
        assertEquals(WriteInstruction::class, factory.create(0x4000.toShort())::class)
        assertEquals(JumpInstruction::class, factory.create(0x5000.toShort())::class)
        assertEquals(ReadKeyboardInstruction::class, factory.create(0x6000.toShort())::class)
        assertEquals(SwitchMemoryInstruction::class, factory.create(0x7000.toShort())::class)
        assertEquals(SkipEqualInstruction::class, factory.create(0x8000.toShort())::class)
        assertEquals(SkipNotEqualInstruction::class, factory.create(0x9000.toShort())::class)
        assertEquals(SetAInstruction::class, factory.create(0xA000.toShort())::class)
        assertEquals(SetTInstruction::class, factory.create(0xB000.toShort())::class)
        assertEquals(ReadTInstruction::class, factory.create(0xC000.toShort())::class)
        assertEquals(ConvertBase10Instruction::class, factory.create(0xD000.toShort())::class)
        assertEquals(ConvertAsciiInstruction::class, factory.create(0xE000.toShort())::class)
        assertEquals(DrawInstruction::class, factory.create(0xF000.toShort())::class)
    }
}
