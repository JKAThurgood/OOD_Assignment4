package d5700

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EmulatorEntryPointTest {
    @Test
    fun haltInstructionStopsExecution() {
        val computer = D5700Computer()
        computer.loadProgram(byteArrayOf(0x00.toByte(), 0x00.toByte()))

        computer.start()
        Thread.sleep(50)
        computer.stop()

        assertEquals(0, computer.cpu.getProgramCounter())
    }
}
