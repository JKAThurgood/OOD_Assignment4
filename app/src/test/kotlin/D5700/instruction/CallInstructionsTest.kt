package d5700.instruction

import d5700.cpu.CPU
import d5700.factory.InstructionFactory
import d5700.io.Screen
import d5700.memory.RAM
import d5700.memory.ROM
import d5700.strategy.RamStrategy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CallInstructionTest {

    private val factory = InstructionFactory()

    private fun cpuWithRam(): CPU {
        return CPU().apply {
            ram = RAM()
            memoryStrategy = RamStrategy(ram!!)
        }
    }

    private fun execute(cpu: CPU, opcode: Int) {
        factory.create(opcode.toShort()).execute(cpu)
    }

    @Test
    fun addInstructionAddsRegisters() {
        val cpu = cpuWithRam()
        cpu.registers[0] = 10
        cpu.registers[1] = 5

        execute(cpu, 0x1010)

        assertEquals(15, cpu.registers[0])
    }

    @Test
    fun subInstructionSubtractsRegisters() {
        val cpu = cpuWithRam()
        cpu.registers[0] = 10
        cpu.registers[1] = 3

        execute(cpu, 0x2010)

        assertEquals(7, cpu.registers[0])
    }

    @Test
    fun storeInstructionStoresImmediateValueInRegister() {
        val cpu = cpuWithRam()
        cpu.address = 20
        cpu.registers[0] = 55

        execute(cpu, 0x0010)

        assertEquals(16.toByte(), cpu.registers[0])
    }

    @Test
    fun readInstructionReadsMemory() {
        val cpu = cpuWithRam()
        cpu.address = 20
        cpu.ram!!.write(20, 42)

        execute(cpu, 0x3000)

        assertEquals(42, cpu.registers[0])
    }

    @Test
    fun writeInstructionWritesRegisterToMemory() {
        val cpu = cpuWithRam()
        cpu.address = 20
        cpu.registers[0] = 99

        execute(cpu, 0x4000)

        assertEquals(99, cpu.ram!!.read(20))
    }

    @Test
    fun jumpInstructionSetsProgramCounter() {
        val cpu = cpuWithRam()
        cpu.address = 100
        cpu.pc = 20

        execute(cpu, 0x5000)

        assertEquals(100, cpu.pc)
    }

    @Test
    fun switchMemoryChangesMemoryStrategy() {
        val cpu = cpuWithRam()
        cpu.rom = ROM(writable = true)

        val before = cpu.memoryStrategy

        execute(cpu, 0x7000)

        assertTrue(cpu.memoryStrategy !== before)
    }

    @Test
    fun skipEqualSkipsInstruction() {
        val cpu = cpuWithRam()
        cpu.pc = 10
        cpu.registers[0] = 5
        cpu.registers[1] = 5

        execute(cpu, 0x8010)

        assertEquals(14, cpu.pc)
    }

    @Test
    fun skipNotEqualSkipsInstruction() {
        val cpu = cpuWithRam()
        cpu.pc = 10
        cpu.registers[0] = 5
        cpu.registers[1] = 6

        execute(cpu, 0x9010)

        assertEquals(14, cpu.pc)
    }

    @Test
    fun setAStoresAddress() {
        val cpu = cpuWithRam()
        cpu.registers[0] = 50
        execute(cpu, 0xA000)

        assertEquals(50, cpu.address)
    }

    @Test
    fun setTStoresTimer() {
        val cpu = cpuWithRam()
        cpu.registers[0] = 25

        execute(cpu, 0xB190)

        assertEquals(25.toByte(), cpu.timer)
    }

    @Test
    fun readTReadsTimer() {
        val cpu = cpuWithRam()
        cpu.timer = 33

        execute(cpu, 0xC000)

        assertEquals(33, cpu.registers[0])
    }

    @Test
    fun convertBase10StoresDigits() {
        val cpu = cpuWithRam()
        cpu.address = 50
        cpu.registers[0] = 253.toByte()

        execute(cpu, 0xD000)

        assertEquals(2.toByte(), cpu.ram!!.read(50))
        assertEquals(5.toByte(), cpu.ram!!.read(51))
        assertEquals(3.toByte(), cpu.ram!!.read(52))
    }

    @Test
    fun convertAsciiConvertsHexDigit() {
        val cpu = cpuWithRam()
        cpu.registers[0] = 10

        execute(cpu, 0xE010)

        assertEquals('A'.code.toByte(), cpu.registers[1])
    }

    @Test
    fun drawInstructionUpdatesScreen() {
        val cpu = cpuWithRam()
        val screen = Screen.instance()

        screen.clear()

        cpu.display = screen
        cpu.registers[0] = 'X'.code.toByte()
        cpu.registers[1] = 2
        cpu.registers[2] = 3

        execute(cpu, 0xF023)

        assertEquals(
            'X'.code.toByte(),
            screen.render()[19]
        )
    }

    @Test
    fun haltInstructionStopsCPU() {
        val cpu = cpuWithRam()

        try {
            execute(cpu, 0x0000)
        } catch (_: IllegalStateException) {
            // expected
        }

        assertTrue(cpu.isHalted())
    }
}