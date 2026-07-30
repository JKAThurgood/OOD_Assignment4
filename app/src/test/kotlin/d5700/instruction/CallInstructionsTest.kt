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
            attachDevices(RAM(), null, null, null)
            setMemoryStrategy(RamStrategy(getRam()!!))
        }
    }

    private fun execute(cpu: CPU, opcode: Int) {
        factory.create(opcode.toShort()).execute(cpu)
    }

    @Test
    fun addInstructionAddsRegisters() {
        val cpu = cpuWithRam()
        cpu.setRegister(0, 10)
        cpu.setRegister(1, 5)

        execute(cpu, 0x1010)

        assertEquals(15, cpu.getRegister(0))
    }

    @Test
    fun subInstructionSubtractsRegisters() {
        val cpu = cpuWithRam()
        cpu.setRegister(0, 10)
        cpu.setRegister(1, 3)

        execute(cpu, 0x2010)

        assertEquals(7, cpu.getRegister(0))
    }

    @Test
    fun storeInstructionStoresImmediateValueInRegister() {
        val cpu = cpuWithRam()
        cpu.setAddress(20)
        cpu.setRegister(0, 55)

        execute(cpu, 0x0010)

        assertEquals(16.toByte(), cpu.getRegister(0))
    }

    @Test
    fun readInstructionReadsMemory() {
        val cpu = cpuWithRam()
        cpu.setAddress(20)
        cpu.getRam()!!.write(20, 42)

        execute(cpu, 0x3000)

        assertEquals(42, cpu.getRegister(0))
    }

    @Test
    fun writeInstructionWritesRegisterToMemory() {
        val cpu = cpuWithRam()
        cpu.setAddress(20)
        cpu.setRegister(0, 99)

        execute(cpu, 0x4000)

        assertEquals(99, cpu.getRam()!!.read(20))
    }

    @Test
    fun jumpInstructionSetsProgramCounter() {
        val cpu = cpuWithRam()
        cpu.setProgramCounter(20)

        execute(cpu, 0x5064) // jump to address 100 (0x64)

        assertEquals(100, cpu.getProgramCounter())
    }

    @Test
    fun switchMemoryChangesMemoryStrategy() {
        val cpu = cpuWithRam()
        cpu.setRom(ROM(writable = true))

        val before = cpu.getMemoryStrategy()

        execute(cpu, 0x7000)

        assertTrue(cpu.getMemoryStrategy() !== before)
    }

    @Test
    fun skipEqualSkipsInstruction() {
        val cpu = cpuWithRam()
        cpu.setProgramCounter(10)
        cpu.setRegister(0, 5)
        cpu.setRegister(1, 5)

        execute(cpu, 0x8010)

        assertEquals(14, cpu.getProgramCounter())
    }

    @Test
    fun skipNotEqualSkipsInstruction() {
        val cpu = cpuWithRam()
        cpu.setProgramCounter(10)
        cpu.setRegister(0, 5)
        cpu.setRegister(1, 6)

        execute(cpu, 0x9010)

        assertEquals(14, cpu.getProgramCounter())
    }

    @Test
    fun setAStoresAddress() {
        val cpu = cpuWithRam()

        execute(cpu, 0xA032) // A = 50

        assertEquals(50, cpu.getAddress())
    }

    @Test
    fun setTStoresTimer() {
        val cpu = cpuWithRam()
        cpu.setRegister(0, 25)

        execute(cpu, 0xB190)

        assertEquals(25.toByte(), cpu.getTimer())
    }

    @Test
    fun readTReadsTimer() {
        val cpu = cpuWithRam()
        cpu.setTimer(33)

        execute(cpu, 0xC000)

        assertEquals(33, cpu.getRegister(0))
    }

    @Test
    fun convertBase10StoresDigits() {
        val cpu = cpuWithRam()
        cpu.setAddress(50)
        cpu.setRegister(0, 253.toByte())

        execute(cpu, 0xD000)

        assertEquals(2.toByte(), cpu.getRam()!!.read(50))
        assertEquals(5.toByte(), cpu.getRam()!!.read(51))
        assertEquals(3.toByte(), cpu.getRam()!!.read(52))
    }

    @Test
    fun convertAsciiConvertsHexDigit() {
        val cpu = cpuWithRam()
        cpu.setRegister(0, 10)

        execute(cpu, 0xE010)

        assertEquals('A'.code.toByte(), cpu.getRegister(1))
    }

    @Test
    fun drawInstructionUpdatesScreen() {
        val cpu = cpuWithRam()
        val screen = Screen()

        screen.clear()

        cpu.setDisplay(screen)
        cpu.setRegister(0, 'X'.code.toByte())
        cpu.setRegister(1, 2)
        cpu.setRegister(2, 3)

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