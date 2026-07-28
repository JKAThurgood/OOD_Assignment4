package D5700.factory

import D5700.instruction.AddInstruction
import D5700.instruction.ConvertAsciiInstruction
import D5700.instruction.ConvertBase10Instruction
import D5700.instruction.DrawInstruction
import D5700.instruction.HaltInstruction
import D5700.instruction.Instruction
import D5700.instruction.JumpInstruction
import D5700.instruction.ReadInstruction
import D5700.instruction.ReadKeyboardInstruction
import D5700.instruction.ReadTInstruction
import D5700.instruction.SetAInstruction
import D5700.instruction.SetTInstruction
import D5700.instruction.SkipEqualInstruction
import D5700.instruction.SkipNotEqualInstruction
import D5700.instruction.StoreInstruction
import D5700.instruction.SubInstruction
import D5700.instruction.SwitchMemoryInstruction
import D5700.instruction.WriteInstruction

class InstructionFactory {
    fun create(opcode: Short): Instruction {
        val nibble = (opcode.toInt() ushr 12) and 0xF
        return when (nibble) {
            0x0 -> if (opcode.toInt() == 0x0000) HaltInstruction() else StoreInstruction()
            0x1 -> AddInstruction()
            0x2 -> SubInstruction()
            0x3 -> ReadInstruction()
            0x4 -> WriteInstruction()
            0x5 -> JumpInstruction()
            0x6 -> ReadKeyboardInstruction()
            0x7 -> SwitchMemoryInstruction()
            0x8 -> SkipEqualInstruction()
            0x9 -> SkipNotEqualInstruction()
            0xA -> SetAInstruction()
            0xB -> SetTInstruction()
            0xC -> ReadTInstruction()
            0xD -> ConvertBase10Instruction()
            0xE -> ConvertAsciiInstruction()
            0xF -> DrawInstruction()
            else -> throw IllegalArgumentException("Invalid opcode: $opcode")
        }
    }
}
