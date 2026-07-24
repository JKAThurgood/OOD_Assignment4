package D5700.instruction

import D5700.cpu.CPU

class SkipNotEqualInstruction : Instruction() {
    override fun perform(cpu: CPU) {
        if (cpu.registers[0] != cpu.registers[1]) {
            markSkip()
        }
    }
}
