package d5700.strategy

import d5700.cpu.CPU
import d5700.memory.RAM

class RamStrategy(private val ram: RAM) : MemoryStrategy {
    override fun read(cpu: CPU): Byte = ram.read(cpu.address)

    override fun write(cpu: CPU, value: Byte) {
        ram.write(cpu.address, value)
    }
}
