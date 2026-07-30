package d5700.instruction

import d5700.cpu.CPU

class SetTInstruction : Instruction() {
    private var value = 0

    override fun decode(opcode: Short) {
        value = (opcode.toInt() ushr 4) and 0xFF
    }

    override fun perform(cpu: CPU) {
        cpu.setTimer(value.toByte())
    }
}