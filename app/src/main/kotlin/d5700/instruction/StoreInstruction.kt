package d5700.instruction

import d5700.cpu.CPU

class StoreInstruction : Instruction() {
    private var register = 0
    private var value: Byte = 0

    override fun decode(opcode: Short) {
        register = (opcode.toInt() ushr 8) and 0x7
        value = (opcode.toInt() and 0xFF).toByte()
    }

    override fun perform(cpu: CPU) {
        cpu.setRegister(register, value)
    }
}