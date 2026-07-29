package D5700.instruction

import D5700.cpu.CPU

class ReadTInstruction : Instruction() {
    private var destinationRegister = 0

    override fun decode(opcode: Short) {
        destinationRegister = (opcode.toInt() ushr 8) and 0x7
    }

    override fun perform(cpu: CPU) {
        cpu.registers[destinationRegister] = cpu.timer
    }
}