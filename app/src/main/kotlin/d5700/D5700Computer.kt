package D5700

import d5700.cpu.CPU
import d5700.io.Keyboard
import d5700.io.Screen
import d5700.memory.RAM
import d5700.memory.ROM
import d5700.scheduling.CpuScheduler
import d5700.scheduling.TimerService
import d5700.strategy.RamStrategy

class D5700Computer {
    val cpu = CPU()
    val ram = RAM()
    val rom = ROM(writable = true)
    val screen = Screen.instance()
    val keyboard = Keyboard()
    private val cpuScheduler = CpuScheduler(cpu)
    private val timerService = TimerService(cpu)

    init {
        cpu.ram = ram
        cpu.memoryStrategy = RamStrategy(ram)
        cpu.rom = rom
        cpu.display = screen
        cpu.input = keyboard
    }

    fun loadProgram(data: ByteArray) {
        require(data.size <= 4096) { "Program data must fit in ROM" }
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
        cpu.pc = 0
        cpu.address = 0
        cpu.timer = 0
        cpu.registers.fill(0)
        cpu.rom = rom
        cpu.ram = ram
        cpu.memoryStrategy = RamStrategy(ram)
        cpu.display = screen
        cpu.input = keyboard
        screen.clear()
    }
}
