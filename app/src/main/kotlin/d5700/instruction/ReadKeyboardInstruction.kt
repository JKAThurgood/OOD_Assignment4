package d5700.instruction

import d5700.cpu.CPU

class ReadKeyboardInstruction : Instruction() {
    private var destinationRegister = 0

    override fun decode(opcode: Short) {
        destinationRegister = (opcode.toInt() ushr 8) and 0x7
    }

    override fun perform(cpu: CPU) {
        println("Waiting for keyboard input...")
        cpu.writeRegister(
            destinationRegister,
            cpu.getInput()?.readHexByte()
                ?: throw IllegalStateException("Input device is not set")
        )
    }
}