package d5700

import d5700.cpu.CPU
import d5700.hardware.Hardware
import d5700.io.Display
import d5700.io.Keyboard
import d5700.io.Screen
import d5700.memory.RAM
import d5700.memory.ROM
import d5700.scheduling.CpuScheduler
import d5700.scheduling.TimerService

class D5700Computer(
    display: Display? = null,
    inputDevice: Keyboard? = null
) {
    private val rom = ROM(writable = true)
    private val ram = RAM()

    private val screen: Display = display ?: Screen()

    val cpu = CPU(
        Hardware(
            ram = ram,
            rom = rom,
            display = screen,
            input = inputDevice ?: Keyboard()
        )
    )

    private val cpuScheduler = CpuScheduler(cpu)
    private val timerService = TimerService(cpu)

    fun loadProgram(data: ByteArray) {
        rom.loadProgram(data)
    }

    fun run(program: ByteArray) {
        loadProgram(program)
        reset()
        start()

        while (!cpu.isHalted()) {
            Thread.sleep(1)
        }

        stop()
    }

    fun start() {
        cpuScheduler.start()
        timerService.start()
    }

    fun stop() {
        cpuScheduler.stop()
        timerService.stop()
    }

    fun reset() {
        cpu.resetState()
        screen.clear()
    }
}