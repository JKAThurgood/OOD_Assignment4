package d5700.instruction

import d5700.cpu.CPU

class SetAInstruction : Instruction() {

    private var addressValue = 0

    override fun decode(opcode: Short) {
        addressValue = opcode.toInt() and 0xFFF
    }

    override fun perform(cpu: CPU) {
        cpu.setAddress(addressValue)
    }
}