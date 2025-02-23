package net.cakemc.veric.error

import net.cakemc.veric.SyntaxPosition
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.max
import kotlin.streams.asStream

object ErrorUtil {

    fun createError(error: SyntaxPosition, message: String): String? {
        return try {
            createError(error, Files.readString(Path.of(error.file)), message)
        } catch (e: IOException) {
            e.message
        }
    }

    fun createError(error: SyntaxPosition, content: String?, message: String?): String {
        val errorLine = error.end.line + 1
        val errorStart = error.start.character
        val errorEnd = error.end.character
        val columns = max(1.0, (errorEnd - errorStart).toDouble()).toInt()
        val padSize = max(
            1.0,
            (floor(log10(errorLine.toDouble())).toInt() + 1).toDouble()
        ).toInt()

        val numPadding = " ".repeat(padSize)
        val numFormat = error.end.line.toString()
        val errPadding = " ".repeat(errorStart)

        val sb = StringBuilder()

        if (content != null) {
            val lines = content.lineSequence().asStream().toList()
            val errString = lines[errorLine - 1]

            sb.append("\n$numPadding |\n")
            sb.append("$numFormat | $errString\n")
            sb.append("$numPadding | ${errPadding}${"^".repeat(columns)}\n")
            sb.append("$numPadding | ${errPadding}${message}\n")
            sb.append("$numPadding |")
        } else {
            sb.append("\n$numPadding |\n")
            sb.append("$numFormat | $message\n")
            sb.append("$numPadding |")
        }

        return sb.toString()
    }

    fun modifyException(exception: Exception, errorMessage: String?): RuntimeException {
        val stackTraceElements: MutableList<StackTraceElement> = ArrayList()
        for (stackTraceElement in exception.stackTrace) {
            if (stackTraceElement.methodName.contains("createParseException")) continue
            if (stackTraceElement.methodName.contains("tryMatchOrError")) continue

            stackTraceElements.add(stackTraceElement)
        }

        val runtimeException = RuntimeException(errorMessage)
        runtimeException.stackTrace = stackTraceElements.toTypedArray<StackTraceElement>()

        return runtimeException
    }

    fun createFullError(error: SyntaxPosition, content: String?, message: String?): String {
        val sb = StringBuilder()

        val position = error.start
        sb.append("(").append(error.file).append(") (line: ").append(position.line + 1).append(", column: ")
            .append(position.character + 1).append("): ")
            .append(createError(error, content, message))

        return sb.toString()
    }
}