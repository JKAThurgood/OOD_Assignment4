package D5700.instruction

import D5700.cpu.CPU

class JumpInstruction : Instruction() {
    override fun perform(cpu: CPU) {
        cpu.pc = cpu.address
    }

    override fun updateProgramCounter(cpu: CPU) {
        // Jump instructions do not advance the program counter automatically.
    }
}
