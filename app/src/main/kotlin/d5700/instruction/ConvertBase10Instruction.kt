package d5700.instruction

import d5700.cpu.CPU

class ConvertBase10Instruction : Instruction() {

    private var sourceRegister = 0

    override fun decode(opcode: Short) {
        sourceRegister = (opcode.toInt() ushr 8) and 0x7
    }

    override fun perform(cpu: CPU) {
        val value = cpu.readRegister(sourceRegister).toInt() and 0xFF
        val startAddress = cpu.getAddress()

        val hundreds = (value / 100).toByte()
        val tens = ((value / 10) % 10).toByte()
        val ones = (value % 10).toByte()

        cpu.writeMemory(hundreds)

        cpu.loadAddress(startAddress + 1)
        cpu.writeMemory(tens)

        cpu.loadAddress(startAddress + 2)
        cpu.writeMemory(ones)

        cpu.loadAddress(startAddress)
    }
}