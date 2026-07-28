package D5700.instruction

import D5700.cpu.CPU

class HaltInstruction : Instruction() {
    override fun perform(cpu: CPU) {
        cpu.terminate("Program terminated")
    }
}
