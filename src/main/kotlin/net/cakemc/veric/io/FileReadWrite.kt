package net.cakemc.veric.io

import net.cakemc.veric.AbstractVeric
import java.nio.file.Path

interface FileReadWrite {

    fun readFromBytes(byteArray: ByteArray): AbstractVeric
    fun toBytes(): ByteArray

    fun readFile(path: Path): AbstractVeric
    fun saveToFile(path: Path)

    fun fromVericString(value: String): AbstractVeric
    fun toVericString(): String

}