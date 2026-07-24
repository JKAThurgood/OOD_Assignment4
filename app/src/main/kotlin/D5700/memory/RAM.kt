package D5700.memory

class RAM : MemoryDevice {
    private val data = ByteArray(4096)

    override fun read(address: Int): Byte {
        validateAddress(address)
        return data[address]
    }

    override fun write(address: Int, value: Byte) {
        validateAddress(address)
        data[address] = value
    }

    private fun validateAddress(address: Int) {
        require(address in 0..4095) { "Address must be between 0 and 4095" }
    }
}
