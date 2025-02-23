package net.cakemc.veric.error

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

object FileUtils {
    private val FILE_CONTENT: MutableMap<File, String> = HashMap()

    /**
     * Returns the digest used for checksums
     */
    val checksumDigest: MessageDigest?

    init {
        var shaDigest: MessageDigest? = null
        try {
            shaDigest = MessageDigest.getInstance("SHA-1")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            System.exit(0)
        }

        checksumDigest = shaDigest
    }

    @Deprecated("")
    fun makeAbsolute(file: File?): File? {
        var file = file ?: return null
        file = file.absoluteFile

        return try {
            file.canonicalFile
        } catch (e: IOException) {
            file
        }
    }

    fun readFile(path: File): String? {
        if (FILE_CONTENT.containsKey(path)) return FILE_CONTENT[path]

        val content = StringBuilder()
        var scanner: Scanner? = null
        try {
            scanner = Scanner(path)
        } catch (e: FileNotFoundException) {
            throw RuntimeException(e)
        }
        while (scanner.hasNext()) content.append(scanner.nextLine())
            .append('\n')
        scanner.close()

        FILE_CONTENT[path] = content.toString()
        return content.toString()
    }

    fun getDataChecksum(bytes: ByteArray?): String {
        val digest = checksumDigest ?: return ""

        digest.update(bytes)

        return digest.digest().joinToString("") { "%02x".format(it) }
    }
}