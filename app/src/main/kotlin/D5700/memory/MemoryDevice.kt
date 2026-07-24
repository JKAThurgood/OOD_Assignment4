package D5700.memory

interface MemoryDevice {
    fun read(address: Int): Byte

    fun write(address: Int, value: Byte)
}
