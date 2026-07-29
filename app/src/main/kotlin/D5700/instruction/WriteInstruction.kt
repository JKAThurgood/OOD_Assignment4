package D5700.instruction

import D5700.cpu.CPU

class WriteInstruction : Instruction() {
    private var sourceRegister = 0

    override fun decode(opcode: Short) {
        sourceRegister = (opcode.toInt() ushr 8) and 0x7
    }

    override fun perform(cpu: CPU) {
        cpu.memoryStrategy?.write(cpu, cpu.registers[sourceRegister])
            ?: throw IllegalStateException("Memory strategy not set")
    }
}