package D5700.strategy

import D5700.cpu.CPU
import D5700.memory.ROM

class RomStrategy(private val rom: ROM) : MemoryStrategy {
    override fun read(cpu: CPU): Byte = rom.read(cpu.address)

    override fun write(cpu: CPU, value: Byte) {
        rom.write(cpu.address, value)
    }
}
