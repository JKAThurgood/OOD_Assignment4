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
    val ram = RAM()
    val rom = ROM(writable = true)
    val screen: Display = display ?: Screen()
    private val keyboard: Keyboard = inputDevice ?: Keyboard()

    private val hardware = Hardware(
        ram = ram,
        rom = rom,
        display = screen,
        input = keyboard
    )

    val cpu = CPU(hardware)

    private val cpuScheduler = CpuScheduler(cpu)
    private val timerService = TimerService(cpu)

    fun loadProgram(data: ByteArray) {
        require(data.size <= 4096) {
            "Program data must fit in ROM"
        }

        for (index in data.indices) {
            rom.write(index, data[index])
        }
    }

    fun start() {
        if (cpu.isHalted()) {
            cpu.terminate("Program already halted")
        }

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