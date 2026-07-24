package D5700.strategy

import D5700.cpu.CPU

interface MemoryStrategy {
    fun read(cpu: CPU): Byte

    fun write(cpu: CPU, value: Byte)
}
