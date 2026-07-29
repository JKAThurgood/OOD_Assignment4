package d5700.strategy

import d5700.cpu.CPU
import d5700.memory.RAM
import d5700.memory.ROM
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MemoryStrategyTest {
    @Test
    fun ramStrategyUsesCpuAddressRegister() {
        val cpu = CPU().apply { address = 10 }
        val ram = RAM()
        val strategy = RamStrategy(ram)

        ram.write(10, 5)
        assertEquals(5, strategy.read(cpu).toInt())

        strategy.write(cpu, 7)
        assertEquals(7, ram.read(10).toInt())
    }

    @Test
    fun romStrategyUsesCpuAddressRegister() {
        val cpu = CPU().apply { address = 20 }
        val rom = ROM(writable = true)
        val strategy = RomStrategy(rom)

        strategy.write(cpu, 9)
        assertEquals(9, strategy.read(cpu).toInt())
    }
}
