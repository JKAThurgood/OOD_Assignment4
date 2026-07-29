package D5700.instruction

import D5700.cpu.CPU

class SetTInstruction : Instruction() {
    private var value = 0

    override fun decode(opcode: Short) {
        value = (opcode.toInt() ushr 4) and 0xFF
    }

    override fun perform(cpu: CPU) {
        cpu.timer = value.toByte()
    }
}