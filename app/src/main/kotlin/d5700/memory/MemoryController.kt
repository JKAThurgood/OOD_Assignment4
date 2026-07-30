package d5700.memory

import d5700.cpu.CPU
import d5700.hardware.Hardware
import d5700.strategy.MemoryStrategy
import d5700.strategy.RamStrategy

class MemoryController(
    private val hardware: Hardware
) {

    private var strategy: MemoryStrategy? =
        RamStrategy(hardware.ram)

    fun read(cpu: CPU): Byte {
        return strategy?.read(cpu)
            ?: throw IllegalStateException("Memory strategy not set")
    }

    fun write(cpu: CPU, value: Byte) {
        strategy?.write(cpu, value)
            ?: throw IllegalStateException("Memory strategy not set")
    }

    fun switchMode() {
        strategy = strategy?.next(
            hardware.ram,
            hardware.rom
        ) ?: RamStrategy(hardware.ram)
    }

    fun reset() {
        strategy = RamStrategy(hardware.ram)
    }

    fun hasMemory(): Boolean = strategy != null
}