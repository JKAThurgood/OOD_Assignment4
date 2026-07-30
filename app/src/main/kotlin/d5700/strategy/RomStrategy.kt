package d5700.strategy

import d5700.cpu.CPU
import d5700.memory.MemoryDevice

class RomStrategy(
    private val rom: MemoryDevice
) : MemoryStrategy {

    override fun read(cpu: CPU): Byte =
        rom.read(cpu.getAddress())

    override fun write(cpu: CPU, value: Byte) =
        rom.write(cpu.getAddress(), value)

    override fun next(
        ram: MemoryDevice?,
        rom: MemoryDevice?
    ): MemoryStrategy {
        return RamStrategy(
            ram ?: throw IllegalStateException("RAM not set")
        )
    }
}