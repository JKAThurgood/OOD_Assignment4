package D5700.instruction

import D5700.cpu.CPU

class ConvertBase10Instruction : Instruction() {
    override fun perform(cpu: CPU) {
        cpu.registers[0] = cpu.registers[0].toInt().toString(16).toByte()
    }
}
