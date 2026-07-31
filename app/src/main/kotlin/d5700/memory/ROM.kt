package d5700.memory

class ROM(private val writable: Boolean = false) : MemoryDevice {
    private val data = ByteArray(4096)

    override fun read(address: Int): Byte {
        validateAddress(address)
        return data[address]
    }

    override fun write(address: Int, value: Byte) {
        validateAddress(address)

        if (!writable) {
            throw IllegalStateException("ROM is read-only")
        }

        data[address] = value
    }

    fun loadProgram(program: ByteArray) {
        require(program.size <= data.size) {
            "Program data must fit in ROM"
        }

        for (index in program.indices) {
            data[index] = program[index]
        }
    }

    private fun validateAddress(address: Int) {
        require(address in 0..4095) {
            "Address must be between 0 and 4095"
        }
    }
}