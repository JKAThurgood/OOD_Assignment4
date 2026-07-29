package D5700.instruction

import D5700.cpu.CPU

class SwitchMemoryInstruction : Instruction() {
    override fun perform(cpu: CPU) {
        cpu.switchMemory()
    }
}
