package D5700

import java.io.File
import java.io.IOException

fun main() {
    print("Enter the path to a .d5700 program file: ")
    System.out.flush()

    val path = readln().trim()

    if (path.isEmpty()) {
        println("No path provided.")
        return
    }

    val file = File(path)
    if (!file.exists() || !file.isFile) {
        println("File not found: $path")
        return
    }

    val lines = try {
        file.readLines().filter { it.isNotBlank() }
    } catch (_: IOException) {
        println("Unable to read file: $path")
        return
    }

    val program = lines.flatMap { line ->
        val value = line.trim().toInt(16)
        listOf((value ushr 8).toByte(), value.toByte())
    }.toByteArray()

    val computer = D5700Computer()
    computer.loadProgram(program)
    computer.start()

    while (!computer.cpu.isHalted()) {
        Thread.sleep(10)
    }

    computer.stop()
    println("Program completed.")
}