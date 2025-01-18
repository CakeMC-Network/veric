package net.cakemc.veric

object StringUtils {
    fun regexEscape(string: String?): String? {
        if (string == null) return null

        val sb = StringBuilder()

        for (i in 0 until string.length) {
            val c = string[i]

            when (c) {
                '\u0000' -> {
                    sb.append("\\0")
                    continue
                }

                '\n' -> {
                    sb.append("\\n")
                    continue
                }

                '\r' -> {
                    sb.append("\\r")
                    continue
                }

                '\t' -> {
                    sb.append("\\t")
                    continue
                }

                '\\' -> {
                    sb.append("\\\\")
                    continue
                }

                '^', '$', '?', '|', '*', '/', '+', '.', '(', ')', '[', ']', '{', '}' -> {
                    sb.append("\\").append(c)
                    continue
                }
            }

            if (c.code > 0xff) { // Unicode
                sb.append("\\u").append(toHexString(c.code.toLong(), 4))
                continue
            }

            if (Character.isISOControl(c)) { // Control character
                sb.append("\\x").append(toHexString(c.code.toLong(), 2))
                continue
            }

            sb.append(c)
        }

        return sb.toString()
    }

    fun toHexString(value: Long, length: Int): String {
        require(length >= 1) { "The minimum length of the returned string cannot be less than one." }
        return String.format("%0" + length + "x", value)
    }
}