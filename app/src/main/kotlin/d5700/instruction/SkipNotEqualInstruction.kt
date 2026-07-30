package d5700.instruction

import d5700.cpu.CPU

class SkipNotEqualInstruction : Instruction() {
    override fun perform(cpu: CPU) {
        if (cpu.readRegister(0) != cpu.readRegister(1)) {
            markSkip()
        }
    }
}
