package D5700.io

interface Display {
    fun draw(ascii: Byte, row: Int, column: Int)
    fun render()
    fun clear()
}
