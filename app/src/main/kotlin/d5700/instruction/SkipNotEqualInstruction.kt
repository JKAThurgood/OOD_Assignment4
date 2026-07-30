package d5700.instruction

import d5700.cpu.CPU

class SkipNotEqualInstruction : Instruction() {
    override fun perform(cpu: CPU) {
        if (cpu.getRegister(0) != cpu.getRegister(1)) {
            markSkip()
        }
    }
}
