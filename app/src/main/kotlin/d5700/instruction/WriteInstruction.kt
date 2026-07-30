package d5700.instruction

import d5700.cpu.CPU

class WriteInstruction : Instruction() {
    private var sourceRegister = 0

    override fun decode(opcode: Short) {
        sourceRegister = (opcode.toInt() ushr 8) and 0x7
    }

    override fun perform(cpu: CPU) {
        cpu.writeMemory(cpu.readRegister(sourceRegister))
    }
}