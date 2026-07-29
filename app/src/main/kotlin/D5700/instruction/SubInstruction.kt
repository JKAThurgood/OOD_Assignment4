package D5700.instruction

import D5700.cpu.CPU

class SubInstruction : Instruction() {
    private var registerX = 0
    private var registerY = 0
    private var registerZ = 0

    override fun decode(opcode: Short) {
        registerX = (opcode.toInt() ushr 8) and 0x7
        registerY = (opcode.toInt() ushr 4) and 0x7
        registerZ = opcode.toInt() and 0x7
    }

    override fun perform(cpu: CPU) {
        cpu.registers[registerZ] = (
                cpu.registers[registerX].toInt() -
                        cpu.registers[registerY].toInt()
                ).toByte()
    }
}