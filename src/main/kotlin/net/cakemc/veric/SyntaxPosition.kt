package net.cakemc.veric

@JvmRecord
data class SyntaxPosition(val file: String, val start: FilePosition, val end: FilePosition) {
    companion object {
        fun empty(): SyntaxPosition {
            return SyntaxPosition("unknown file" , FilePosition(0, 0), FilePosition(0, 0))
        }

        fun of(file: String, start: FilePosition, end: FilePosition): SyntaxPosition {
            return SyntaxPosition(file , start, end)
        }
    }
}
