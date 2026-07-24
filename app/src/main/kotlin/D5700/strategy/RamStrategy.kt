package D5700.strategy

import D5700.cpu.CPU
import D5700.memory.RAM

class RamStrategy(private val ram: RAM) : MemoryStrategy {
    override fun read(cpu: CPU): Byte = ram.read(cpu.address)

    override fun write(cpu: CPU, value: Byte) {
        ram.write(cpu.address, value)
    }
}
