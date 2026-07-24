package D5700.instruction

import D5700.cpu.CPU

class ReadKeyboardInstruction : Instruction() {
    override fun perform(cpu: CPU) {
        cpu.registers[0] = 0
    }
}
