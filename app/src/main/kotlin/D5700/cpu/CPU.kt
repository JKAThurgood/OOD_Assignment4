package D5700.cpu

import D5700.factory.InstructionFactory
import D5700.memory.MemoryDevice
import D5700.strategy.MemoryStrategy

class CPU {
    private val instructionFactory = InstructionFactory()

    val registers: ByteArray = ByteArray(8)
    var pc: Int = 0
    var timer: Byte = 0
    var address: Int = 0
    var memoryFlag: Boolean = false
    var memoryStrategy: MemoryStrategy? = null
    var rom: MemoryDevice? = null

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

    fun cycle() {
        if (terminated) {
            return
        }
        val instruction = instructionFactory.create(fetchInstruction())
        instruction.execute(this)
    }

    fun fetchInstruction(): Short {
        require(pc % 2 == 0) { "PC must be even" }
        val programMemory = rom ?: throw IllegalStateException("ROM is not set")
        val firstByte = programMemory.read(pc).toInt() and 0xFF
        val secondByte = programMemory.read(pc + 1).toInt() and 0xFF
        return ((firstByte shl 8) or secondByte).toShort()
    }

    fun incrementPC() {
        pc += 2
    }

    fun skipInstruction() {
        pc += 4
    }

    fun terminate(message: String = "Program terminated") {
        terminated = true
        throw IllegalStateException(message)
    }
}
