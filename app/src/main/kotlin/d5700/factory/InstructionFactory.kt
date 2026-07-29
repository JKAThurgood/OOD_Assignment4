package d5700.factory

import d5700.instruction.AddInstruction
import d5700.instruction.ConvertAsciiInstruction
import d5700.instruction.ConvertBase10Instruction
import d5700.instruction.DrawInstruction
import d5700.instruction.HaltInstruction
import d5700.instruction.Instruction
import d5700.instruction.JumpInstruction
import d5700.instruction.ReadInstruction
import d5700.instruction.ReadKeyboardInstruction
import d5700.instruction.ReadTInstruction
import d5700.instruction.SetAInstruction
import d5700.instruction.SetTInstruction
import d5700.instruction.SkipEqualInstruction
import d5700.instruction.SkipNotEqualInstruction
import d5700.instruction.StoreInstruction
import d5700.instruction.SubInstruction
import d5700.instruction.SwitchMemoryInstruction
import d5700.instruction.WriteInstruction

class InstructionFactory {
    fun create(opcode: Short): Instruction {
        val nibble = (opcode.toInt() ushr 12) and 0xF
        val instr = when (nibble) {
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
        instr.decode(opcode)
        return instr
    }
}
