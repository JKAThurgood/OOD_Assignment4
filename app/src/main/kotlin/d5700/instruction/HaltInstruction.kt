package d5700.instruction

import d5700.cpu.CPU

class HaltInstruction : Instruction() {
    override fun perform(cpu: CPU) {
        cpu.terminate("Program terminated")
    }
}
