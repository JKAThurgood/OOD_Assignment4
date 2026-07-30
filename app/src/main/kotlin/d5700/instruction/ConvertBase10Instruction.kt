package d5700.instruction

import d5700.cpu.CPU

class ConvertBase10Instruction : Instruction() {

    private var sourceRegister = 0

    override fun decode(opcode: Short) {
        sourceRegister = (opcode.toInt() ushr 8) and 0x7
    }

    override fun perform(cpu: CPU) {
        val value = cpu.getRegister(sourceRegister).toInt() and 0xFF
        val startAddress = cpu.getAddress()

        val hundreds = (value / 100).toByte()
        val tens = ((value / 10) % 10).toByte()
        val ones = (value % 10).toByte()

        cpu.getMemoryStrategy()?.write(cpu, hundreds)
        cpu.setAddress(startAddress + 1)

        cpu.getMemoryStrategy()?.write(cpu, tens)
        cpu.setAddress(startAddress + 2)

        cpu.getMemoryStrategy()?.write(cpu, ones)

        cpu.setAddress(startAddress)
    }
}