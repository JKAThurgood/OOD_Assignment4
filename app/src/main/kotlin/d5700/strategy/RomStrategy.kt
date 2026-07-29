package d5700.strategy

import d5700.cpu.CPU
import d5700.memory.ROM

class RomStrategy(private val rom: ROM) : MemoryStrategy {
    override fun read(cpu: CPU): Byte = rom.read(cpu.address)

    override fun write(cpu: CPU, value: Byte) {
        rom.write(cpu.address, value)
    }
}
