package d5700.instruction

import d5700.cpu.CPU

class DrawInstruction : Instruction() {
    private var charRegister = 0
    private var row = 0
    private var column = 0

    override fun decode(opcode: Short) {
        charRegister = (opcode.toInt() ushr 8) and 0x7
        row = (opcode.toInt() ushr 4) and 0xF
        column = opcode.toInt() and 0xF
    }

    override fun perform(cpu: CPU) {
        cpu.getDisplay()?.draw(
            cpu.readRegister(charRegister),
            row,
            column
        ) ?: throw IllegalStateException("Display is not set")
    }
}