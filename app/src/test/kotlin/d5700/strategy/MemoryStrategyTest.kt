package d5700.strategy

import d5700.cpu.CPU
import d5700.hardware.Hardware
import d5700.io.Keyboard
import d5700.io.Screen
import d5700.memory.RAM
import d5700.memory.ROM
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MemoryStrategyTest {

    @Test
    fun ramStrategyUsesCpuAddressRegister() {
        val ram = RAM()

        val cpu = CPU(
            Hardware(
                ram = ram,
                rom = ROM(writable = true),
                display = Screen(),
                input = Keyboard()
            )
        ).apply {
            loadAddress(20)
        }

        cpu.writeMemory(42)

        assertEquals(42, ram.read(20))
    }

    @Test
    fun romStrategyUsesCpuAddressRegister() {
        val rom = ROM(writable = true)

        val cpu = CPU(
            Hardware(
                ram = RAM(),
                rom = rom,
                display = Screen(),
                input = Keyboard()
            )
        ).apply {
            loadAddress(20)
        }

        val strategy = RomStrategy(rom)

        strategy.write(cpu, 9)

        assertEquals(9, strategy.read(cpu).toInt())
    }
}