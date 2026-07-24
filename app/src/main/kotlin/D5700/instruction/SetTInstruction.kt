package D5700.instruction

import D5700.cpu.CPU

class SetTInstruction : Instruction() {
    override fun perform(cpu: CPU) {
        cpu.timer = cpu.registers[0]
    }
}
