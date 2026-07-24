package D5700.instruction

import D5700.cpu.CPU

class ConvertAsciiInstruction : Instruction() {
    override fun perform(cpu: CPU) {
        cpu.registers[0] = cpu.registers[0].toInt().toChar().code.toByte()
    }
}
