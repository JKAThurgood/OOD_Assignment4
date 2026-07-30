package d5700.instruction

import d5700.cpu.CPU
import d5700.factory.InstructionFactory
import d5700.hardware.Hardware
import d5700.io.Keyboard
import d5700.io.Screen
import d5700.memory.RAM
import d5700.memory.ROM
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CallInstructionTest {

    private val factory = InstructionFactory()

    private fun cpuWithRam(): CPU {
        val ram = RAM()
        val rom = ROM(writable = true)

        return CPU(
            Hardware(
                ram = ram,
                rom = rom,
                display = Screen(),
                input = Keyboard()
            )
        )
    }

    private fun execute(cpu: CPU, opcode: Int) {
        factory.create(opcode.toShort()).execute(cpu)
    }

    @Test
    fun addInstructionAddsRegisters() {
        val cpu = cpuWithRam()
        cpu.writeRegister(0, 10)
        cpu.writeRegister(1, 5)

        execute(cpu, 0x1010)

        assertEquals(15, cpu.readRegister(0))
    }

    @Test
    fun subInstructionSubtractsRegisters() {
        val cpu = cpuWithRam()
        cpu.writeRegister(0, 10)
        cpu.writeRegister(1, 3)

        execute(cpu, 0x2010)

        assertEquals(7, cpu.readRegister(0))
    }

    @Test
    fun storeInstructionStoresImmediateValueInRegister() {
        val cpu = cpuWithRam()

        execute(cpu, 0x0010)

        assertEquals(16.toByte(), cpu.readRegister(0))
    }

    @Test
    fun readInstructionReadsMemory() {
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

        ram.write(20, 42)

        execute(cpu, 0x3000)

        assertEquals(42, cpu.readRegister(0))
    }

    @Test
    fun writeInstructionWritesRegisterToMemory() {
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
            writeRegister(0, 99)
        }

        execute(cpu, 0x4000)

        assertEquals(99, ram.read(20))
    }

    @Test
    fun jumpInstructionSetsProgramCounter() {
        val cpu = cpuWithRam()

        execute(cpu, 0x5064)

        assertEquals(100, cpu.getProgramCounter())
    }

    @Test
    fun switchMemoryChangesMemoryStrategy() {
        val cpu = cpuWithRam()

        assertTrue(cpu.hasMemory())

        cpu.switchMemory()

        assertTrue(cpu.hasMemory())
    }

    @Test
    fun skipEqualSkipsInstruction() {
        val cpu = cpuWithRam()
        cpu.jumpTo(10)
        cpu.writeRegister(0, 5)
        cpu.writeRegister(1, 5)

        execute(cpu, 0x8010)

        assertEquals(14, cpu.getProgramCounter())
    }

    @Test
    fun skipNotEqualSkipsInstruction() {
        val cpu = cpuWithRam()
        cpu.jumpTo(10)
        cpu.writeRegister(0, 5)
        cpu.writeRegister(1, 6)

        execute(cpu, 0x9010)

        assertEquals(14, cpu.getProgramCounter())
    }

    @Test
    fun setAStoresAddress() {
        val cpu = cpuWithRam()

        execute(cpu, 0xA032)

        assertEquals(50, cpu.getAddress())
    }

    @Test
    fun setTStoresTimer() {
        val cpu = cpuWithRam()

        execute(cpu, 0xB190)

        assertEquals(25.toByte(), cpu.readTimer())
    }

    @Test
    fun readTReadsTimer() {
        val cpu = cpuWithRam()
        cpu.setTimer(33.toByte())

        execute(cpu, 0xC000)

        assertEquals(33.toByte(), cpu.readRegister(0))
    }

    @Test
    fun convertBase10StoresDigits() {
        val ram = RAM()
        val cpu = CPU(
            Hardware(
                ram = ram,
                rom = ROM(writable = true),
                display = Screen(),
                input = Keyboard()
            )
        )

        cpu.loadAddress(50)
        cpu.writeRegister(0, 253.toByte())

        execute(cpu, 0xD000)

        assertEquals(2.toByte(), ram.read(50))
        assertEquals(5.toByte(), ram.read(51))
        assertEquals(3.toByte(), ram.read(52))
    }

    @Test
    fun convertAsciiConvertsDecimalDigit() {
        val cpu = cpuWithRam()
        cpu.writeRegister(0, 5)

        execute(cpu, 0xE010)

        assertEquals('5'.code.toByte(), cpu.readRegister(1))
    }

    @Test
    fun convertAsciiConvertsHexLetter() {
        val cpu = cpuWithRam()
        cpu.writeRegister(0, 10)

        execute(cpu, 0xE010)

        assertEquals('A'.code.toByte(), cpu.readRegister(1))
    }

    @Test
    fun drawInstructionUpdatesScreen() {
        val ram = RAM()
        val screen = Screen()

        screen.clear()

        val cpu = CPU(
            Hardware(
                ram = ram,
                rom = ROM(writable = true),
                display = screen,
                input = Keyboard()
            )
        )

        cpu.writeRegister(0, 'X'.code.toByte())
        cpu.writeRegister(1, 2)
        cpu.writeRegister(2, 3)

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