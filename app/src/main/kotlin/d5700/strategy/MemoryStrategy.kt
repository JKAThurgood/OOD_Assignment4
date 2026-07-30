package d5700.strategy

import d5700.cpu.CPU
import d5700.memory.MemoryDevice

interface MemoryStrategy {
    fun read(cpu: CPU): Byte

    fun write(cpu: CPU, value: Byte)

    fun next(
        ram: MemoryDevice?,
        rom: MemoryDevice?
    ): MemoryStrategy
}