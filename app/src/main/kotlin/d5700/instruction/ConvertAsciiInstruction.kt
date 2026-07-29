package d5700.instruction

import d5700.cpu.CPU

class ConvertAsciiInstruction : Instruction() {
    private var sourceRegister = 0
    private var destinationRegister = 0

    override fun decode(opcode: Short) {
        sourceRegister = (opcode.toInt() ushr 8) and 0x7
        destinationRegister = (opcode.toInt() ushr 4) and 0x7
    }

    override fun perform(cpu: CPU) {
        val value = cpu.registers[sourceRegister].toInt() and 0xFF

        require(value <= 0xF) {
            "Value must be a hexadecimal digit"
        }

        cpu.registers[destinationRegister] = when (value) {
            in 0..9 -> ('0'.code + value).toByte()
            in 10..15 -> ('A'.code + (value - 10)).toByte()
            else -> throw IllegalStateException("Invalid hexadecimal digit")
        }
    }
}