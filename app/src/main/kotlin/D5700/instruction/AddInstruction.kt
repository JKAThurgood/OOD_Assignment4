package D5700.instruction

import D5700.cpu.CPU

class AddInstruction : Instruction() {
    private var sourceRegisterX = 0
    private var sourceRegisterY = 0
    private var destinationRegister = 0

    override fun decode(opcode: Short) {
        sourceRegisterX = (opcode.toInt() ushr 8) and 0x7
        sourceRegisterY = (opcode.toInt() ushr 4) and 0x7
        destinationRegister = opcode.toInt() and 0x7
    }

    override fun perform(cpu: CPU) {
        val result = (
                cpu.registers[sourceRegisterX] + cpu.registers[sourceRegisterY]).toByte()

        cpu.registers[destinationRegister] = result
    }
}