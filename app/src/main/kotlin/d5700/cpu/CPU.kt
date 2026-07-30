package d5700.cpu

import d5700.factory.InstructionFactory
import d5700.io.Display
import d5700.io.InputDevice
import d5700.memory.MemoryController
import d5700.memory.MemoryDevice
import d5700.strategy.MemoryStrategy


class CPU {
    private val instructionFactory = InstructionFactory()

    private val registers = ByteArray(8)

    private var pc = 0
    private var timer: Byte = 0
    private var address = 0

    private var memoryController: MemoryController? = null
    private var rom: MemoryDevice? = null
    private var display: Display? = null
    private var input: InputDevice? = null

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
        return memoryController?.hasMemory() == true
    }

    fun readMemory(): Byte {
        return memoryController?.read(this)
            ?: throw IllegalStateException("Memory controller not set")
    }

    fun writeMemory(value: Byte) {
        memoryController?.write(this, value)
            ?: throw IllegalStateException("Memory controller not set")
    }

    fun attachDevices(
        ram: MemoryDevice?,
        rom: MemoryDevice?,
        display: Display?,
        input: InputDevice?
    ) {
        this.rom = rom
        this.display = display
        this.input = input
        this.memoryController = MemoryController(ram, rom)
    }

    fun drawRegister(registerIndex: Int, row: Int, column: Int) {
        display?.draw(
            readRegister(registerIndex),
            row,
            column
        ) ?: throw IllegalStateException("Display is not set")
    }

    fun readInputInto(registerIndex: Int) {
        val value = input?.readHexByte()
            ?: throw IllegalStateException("Input device is not set")

        writeRegister(registerIndex, value)
    }

    fun resetState() {
        pc = 0
        address = 0
        timer = 0
        registers.fill(0)
        terminated = false

        memoryController?.reset()
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

        val programMemory = rom
            ?: throw IllegalStateException("ROM is not set")

        val firstByte = programMemory.read(pc).toInt() and 0xFF
        val secondByte = programMemory.read(pc + 1).toInt() and 0xFF

        return ((firstByte shl 8) or secondByte).toShort()
    }

    fun switchMemory() {
        memoryController?.switchMode()
            ?: throw IllegalStateException("Memory controller not set")
    }

    fun terminate(message: String = "Program terminated") {
        terminated = true
        throw IllegalStateException(message)
    }
}