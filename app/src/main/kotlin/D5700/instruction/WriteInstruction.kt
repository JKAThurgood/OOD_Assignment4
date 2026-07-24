package D5700.instruction

import D5700.cpu.CPU

class WriteInstruction : Instruction() {
    override fun perform(cpu: CPU) {
        cpu.memoryStrategy?.write(cpu, cpu.registers[0])
            ?: throw IllegalStateException("Memory strategy not set")
    }
}
