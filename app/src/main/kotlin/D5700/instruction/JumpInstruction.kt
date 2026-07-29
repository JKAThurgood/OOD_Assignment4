package D5700.instruction

import D5700.cpu.CPU

class JumpInstruction : Instruction() {
    private var address = 0

    override fun decode(opcode: Short) {
        address = opcode.toInt() and 0x0FFF
    }

    override fun perform(cpu: CPU) {
        require(address % 2 == 0) {
            "Jump address must be divisible by 2"
        }

        cpu.pc = address
    }

    override fun updateProgramCounter(cpu: CPU) {
        // Jump instructions do not increment the program counter.
    }
}