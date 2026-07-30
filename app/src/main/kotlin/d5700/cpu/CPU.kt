package d5700.cpu

import d5700.factory.InstructionFactory
import d5700.io.Display
import d5700.io.InputDevice
import d5700.memory.MemoryDevice
import d5700.memory.RAM
import d5700.memory.ROM
import d5700.strategy.MemoryStrategy
import d5700.strategy.RamStrategy
import d5700.strategy.RomStrategy

class CPU {
    private val instructionFactory = InstructionFactory()

    private val registers: ByteArray = ByteArray(8)
    private var pc: Int = 0
    private var timer: Byte = 0
    private var address: Int = 0
    private var memoryStrategy: MemoryStrategy? = null
    private var rom: MemoryDevice? = null
    private var ram: RAM? = null
    private var display: Display? = null
    private var input: InputDevice? = null

    var r0: Byte
        get() = registers[0]
        set(value) {
            registers[0] = value
        }
    var r1: Byte
        get() = registers[1]
        set(value) {
            registers[1] = value
        }
    var r2: Byte
        get() = registers[2]
        set(value) {
            registers[2] = value
        }
    var r3: Byte
        get() = registers[3]
        set(value) {
            registers[3] = value
        }
    var r4: Byte
        get() = registers[4]
        set(value) {
            registers[4] = value
        }
    var r5: Byte
        get() = registers[5]
        set(value) {
            registers[5] = value
        }
    var r6: Byte
        get() = registers[6]
        set(value) {
            registers[6] = value
        }
    var r7: Byte
        get() = registers[7]
        set(value) {
            registers[7] = value
        }

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
        return memoryStrategy != null
    }

    fun readMemory(): Byte {
        return memoryStrategy?.read(this)
            ?: throw IllegalStateException("Memory strategy not set")
    }

    fun writeMemory(value: Byte) {
        memoryStrategy?.write(this, value)
            ?: throw IllegalStateException("Memory strategy not set")
    }

    fun getRom(): MemoryDevice? = rom

    fun getRam(): RAM? = ram


    fun getDisplay(): Display? = display


    fun getInput(): InputDevice? = input


    fun attachDevices(ram: RAM?, rom: MemoryDevice?, display: Display?, input: InputDevice?) {
        this.ram = ram
        this.rom = rom
        this.display = display
        this.input = input
        this.memoryStrategy = if (ram != null) RamStrategy(ram) else null
    }

    fun resetState() {
        pc = 0
        address = 0
        timer = 0
        registers.fill(0)
        terminated = false
        memoryStrategy = if (ram != null) RamStrategy(ram!!) else null
    }

    fun isHalted(): Boolean = terminated

    fun cycle() {
        if (terminated) {
            return
        }

        val opcode = fetchInstruction()
        val instruction = instructionFactory.create(opcode)

        instruction.execute(this)
    }

    fun fetchInstruction(): Short {
        require(pc % 2 == 0) { "PC must be even" }
        val programMemory = rom ?: throw IllegalStateException("ROM is not set")
        val firstByte = programMemory.read(pc).toInt() and 0xFF
        val secondByte = programMemory.read(pc + 1).toInt() and 0xFF
        return ((firstByte shl 8) or secondByte).toShort()
    }

    fun switchMemory() {
        memoryStrategy = when (memoryStrategy) {
            is RamStrategy -> RomStrategy(rom as? ROM ?: throw IllegalStateException("ROM is not set"))
            is RomStrategy -> RamStrategy(ram ?: throw IllegalStateException("RAM is not set"))
            else -> RamStrategy(ram ?: throw IllegalStateException("RAM is not set"))
        }
    }

    fun terminate(message: String = "Program terminated") {
        terminated = true
        throw IllegalStateException(message)
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
}
