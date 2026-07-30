package d5700.memory

import d5700.cpu.CPU
import d5700.strategy.MemoryStrategy
import d5700.strategy.RamStrategy
import d5700.strategy.RomStrategy

class MemoryController(
    private val ram: MemoryDevice?,
    private val rom: MemoryDevice?
) {

    private var strategy: MemoryStrategy? =
        ram?.let { RamStrategy(it) }

    fun read(cpu: CPU): Byte {
        return strategy?.read(cpu)
            ?: throw IllegalStateException("Memory strategy not set")
    }

    fun write(cpu: CPU, value: Byte) {
        strategy?.write(cpu, value)
            ?: throw IllegalStateException("Memory strategy not set")
    }

    fun switchMode() {
        strategy = when (strategy) {
            is RamStrategy ->
                RomStrategy(
                    rom ?: throw IllegalStateException("ROM not set")
                )

            is RomStrategy ->
                RamStrategy(
                    ram ?: throw IllegalStateException("RAM not set")
                )

            null ->
                RamStrategy(
                    ram ?: throw IllegalStateException("RAM not set")
                )

            else ->
                throw IllegalStateException("Unknown memory strategy")
        }
    }

    fun reset() {
        strategy = ram?.let { RamStrategy(it) }
    }

    fun hasMemory(): Boolean = strategy != null
}