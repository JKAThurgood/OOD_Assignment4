package d5700.instruction

import d5700.cpu.CPU

class JumpInstruction : Instruction() {

    private var jumpAddress = 0

    override fun decode(opcode: Short) {
        jumpAddress = opcode.toInt() and 0x0FFF
    }

    override fun perform(cpu: CPU) {
        require(jumpAddress % 2 == 0) {
            "Jump address must be divisible by 2"
        }

        cpu.setProgramCounter(jumpAddress)
    }

    override fun updateProgramCounter(cpu: CPU) {
        // Jump already changed PC
    }
}