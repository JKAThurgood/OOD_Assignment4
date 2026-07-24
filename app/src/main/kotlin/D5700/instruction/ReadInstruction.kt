package D5700.instruction

import D5700.cpu.CPU

class ReadInstruction : Instruction() {
    override fun perform(cpu: CPU) {
        cpu.registers[0] = cpu.memoryStrategy?.read(cpu) ?: throw IllegalStateException("Memory strategy not set")
    }
}
