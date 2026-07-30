package d5700.cpu

import d5700.factory.InstructionFactory
import d5700.hardware.Hardware
import d5700.memory.MemoryController

class CPU(
    private val hardware: Hardware
) {
    private val instructionFactory = InstructionFactory()

    private val registers = ByteArray(8)

    private var pc = 0
    private var timer: Byte = 0
    private var address = 0

    private val memoryController =
        MemoryController(hardware)

    private var terminated = false

    fun readRegister(index: Int): Byte {
        require(index in registers.indices)
        return registers[index]
    }

    fun writeRegister(index: Int, value: Byte) {
        require(index in registers.indices)
        registers[index] = value
    }

    fun getProgramCounter(): Int = pc

    fun jumpTo(address: Int) {
        require(address % 2 == 0) {
            "Program counter must be even"
        }
        pc = address
    }

    fun incrementPC() {
        pc += 2
    }

    fun skipInstruction() {
        pc += 4
    }

    fun setTimer(value: Byte) {
        timer = value
    }

    fun decrementTimer() {
        if (timer > 0) {
            timer--
        }
    }

    fun readTimer(): Byte = timer

    fun getAddress(): Int = address

    fun loadAddress(value: Int) {
        require(value in 0..4095) {
            "Address must be between 0 and 4095"
        }
        address = value
    }

    fun hasMemory(): Boolean {
        return memoryController.hasMemory()
    }

    fun readMemory(): Byte {
        return memoryController.read(this)
    }

    fun writeMemory(value: Byte) {
        memoryController.write(this, value)
    }

    fun drawRegister(registerIndex: Int, row: Int, column: Int) {
        hardware.display.draw(
            readRegister(registerIndex),
            row,
            column
        )
    }

    fun readInputInto(registerIndex: Int) {
        writeRegister(
            registerIndex,
            hardware.input.readHexByte()
        )
    }

    fun resetState() {
        pc = 0
        address = 0
        timer = 0
        registers.fill(0)
        terminated = false

        memoryController.reset()
    }

    fun isHalted(): Boolean = terminated

    fun cycle() {
        if (terminated) {
            return
        }

        val opcode = fetchInstruction()
        instructionFactory.create(opcode).execute(this)
    }

    fun fetchInstruction(): Short {
        require(pc % 2 == 0) {
            "PC must be even"
        }

        val firstByte = hardware.rom.read(pc).toInt() and 0xFF
        val secondByte = hardware.rom.read(pc + 1).toInt() and 0xFF

        return ((firstByte shl 8) or secondByte).toShort()
    }

    fun switchMemory() {
        memoryController.switchMode()
    }

    fun terminate(message: String = "Program terminated") {
        terminated = true
        throw IllegalStateException(message)
    }
}