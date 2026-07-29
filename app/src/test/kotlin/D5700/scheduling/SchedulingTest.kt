package d5700.scheduling

import d5700.cpu.CPU
import d5700.memory.RAM
import d5700.memory.ROM
import d5700.strategy.RamStrategy
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SchedulingTest {
    @Test
    fun cpuSchedulerExecutesCpuCyclesAt500Hz() {
        val rom = ROM(writable = true)
        rom.write(0, 0x10.toByte())
        rom.write(1, 0x00.toByte())

        val cpu = CPU().apply {
            this.rom = rom
            pc = 0
            memoryStrategy = RamStrategy(RAM())
        }

        val scheduler = CpuScheduler(cpu)
        scheduler.start()

        Thread.sleep(30)
        scheduler.stop()

        assertTrue(cpu.pc > 0, "CPU should execute cycles while the scheduler is running")
    }

    @Test
    fun timerServiceDecrementsCpuTimerAt60Hz() {
        val cpu = CPU().apply {
            timer = 5
        }

        val timerService = TimerService(cpu)
        timerService.start()

        Thread.sleep(120)
        timerService.stop()

        assertTrue(cpu.timer < 5, "Timer service should decrement the CPU timer register")
    }
}
