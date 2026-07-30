package d5700.hardware

import d5700.io.Display
import d5700.io.InputDevice
import d5700.memory.MemoryDevice

data class Hardware(
    val ram: MemoryDevice,
    val rom: MemoryDevice,
    val display: Display,
    val input: InputDevice
)