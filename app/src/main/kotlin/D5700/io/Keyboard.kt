package D5700.io

class Keyboard {
    fun readHexByte(): Byte {
        val input = readLine()?.trim().orEmpty()

        if (input.isEmpty()) {
            return 0
        }

        require(input.length <= 2) { "Hex input must be at most 2 digits" }
        require(input.all { it.isDigit() || it.lowercaseChar() in 'a'..'f' }) {
            "Hex input must contain only hexadecimal digits"
        }

        return input.toInt(16).toByte()
    }
}
