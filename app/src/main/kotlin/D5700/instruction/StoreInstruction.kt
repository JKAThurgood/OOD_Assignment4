package D5700.instruction

import D5700.cpu.CPU

class StoreInstruction : Instruction() {
    override fun perform(cpu: CPU) {
        val value = cpu.registers[0]
        val address = cpu.address
        cpu.memoryStrategy?.write(cpu, value)
        cpu.address = address
    }
}
