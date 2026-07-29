package d5700.instruction

import d5700.cpu.CPU

class JumpInstruction : Instruction() {
    override fun perform(cpu: CPU) {
        require(cpu.address % 2 == 0) {
            "Jump address must be divisible by 2"
        }

        cpu.pc = cpu.address
    }

    override fun updateProgramCounter(cpu: CPU) {
        // Jump instructions do not increment the program counter.
    }
}