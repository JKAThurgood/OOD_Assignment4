package d5700

import java.io.File
import java.io.IOException

fun main() {
    print("Enter the path to ROM: ")
    System.out.flush()

    val path = ""


    val file = File("/home/jaket/Documents/Summer2026/OOD/OOD_Assignment4/roms/keyboard.out")
    if (!file.exists() || !file.isFile) {
        println("File not found: $path")
        return
    }

    val program = try {
        file.readBytes()
    } catch (_: IOException) {
        println("Unable to read file: $path")
        return
    }

    val computer = D5700Computer()
    computer.loadProgram(program)
    computer.start()

    while (!computer.cpu.isHalted()) {
        Thread.sleep(10)
    }

    computer.stop()
    println("Program completed.")
}