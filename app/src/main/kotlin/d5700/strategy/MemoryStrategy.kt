package d5700.strategy

import d5700.cpu.CPU

interface MemoryStrategy {
    fun read(cpu: CPU): Byte

    fun write(cpu: CPU, value: Byte)
}
