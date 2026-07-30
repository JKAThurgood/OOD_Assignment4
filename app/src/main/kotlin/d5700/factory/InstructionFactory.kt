package d5700.factory

import d5700.instruction.*
import java.util.function.Supplier

class InstructionFactory {

    private val instructionMap: Map<Int, Supplier<Instruction>> = mapOf(
        0x0 to Supplier { StoreInstruction() },
        0x1 to Supplier { AddInstruction() },
        0x2 to Supplier { SubInstruction() },
        0x3 to Supplier { ReadInstruction() },
        0x4 to Supplier { WriteInstruction() },
        0x5 to Supplier { JumpInstruction() },
        0x6 to Supplier { ReadKeyboardInstruction() },
        0x7 to Supplier { SwitchMemoryInstruction() },
        0x8 to Supplier { SkipEqualInstruction() },
        0x9 to Supplier { SkipNotEqualInstruction() },
        0xA to Supplier { SetAInstruction() },
        0xB to Supplier { SetTInstruction() },
        0xC to Supplier { ReadTInstruction() },
        0xD to Supplier { ConvertBase10Instruction() },
        0xE to Supplier { ConvertAsciiInstruction() },
        0xF to Supplier { DrawInstruction() }
    )

    fun create(opcode: Short): Instruction {
        val value = opcode.toInt() and 0xFFFF
        val nibble = (value ushr 12) and 0xF

        val instruction = if (value == 0x0000) {
            HaltInstruction()
        } else {
            instructionMap[nibble]?.get()
                ?: throw IllegalArgumentException("Invalid opcode: $opcode")
        }

        instruction.decode(opcode)
        return instruction
    }
}