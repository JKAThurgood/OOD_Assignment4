package d5700.strategy

import d5700.cpu.CPU
import d5700.memory.MemoryDevice

class RamStrategy(
    private val ram: MemoryDevice
) : MemoryStrategy {

    override fun read(cpu: CPU): Byte =
        ram.read(cpu.getAddress())

    override fun write(cpu: CPU, value: Byte) =
        ram.write(cpu.getAddress(), value)

    override fun next(
        ram: MemoryDevice?,
        rom: MemoryDevice?
    ): MemoryStrategy {
        return RomStrategy(
            rom ?: throw IllegalStateException("ROM not set")
        )
    }
}