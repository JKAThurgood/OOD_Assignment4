package d5700.instruction

import d5700.cpu.CPU

class SwitchMemoryInstruction : Instruction() {
    override fun decode(opcode: Short) {
        // No operands
    }

    override fun perform(cpu: CPU) {
        cpu.switchMemory()
    }
}