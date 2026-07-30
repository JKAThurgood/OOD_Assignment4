package d5700.instruction

import d5700.cpu.CPU

class SetAInstruction : Instruction() {

    private var address = 0

    override fun decode(opcode: Short) {
        address = opcode.toInt() and 0x0FFF
    }

    override fun perform(cpu: CPU) {
        cpu.loadAddress(address)
    }
}