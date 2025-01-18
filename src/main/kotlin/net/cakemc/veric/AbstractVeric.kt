package net.cakemc.veric

import net.cakemc.veric.entry.VericElement
import net.cakemc.veric.io.FileReadWrite

abstract class AbstractVeric protected constructor(
    var key: String,
    var elements: MutableList<VericElement<*>>
): FileReadWrite {

    abstract fun getInteger(key: String): Int
    abstract fun getDouble(key: String): Double
    abstract fun getFloat(key: String): Float
    abstract fun getLong(key: String): Long
    abstract fun getBoolean(key: String): Boolean
    abstract fun getString(key: String): String
    abstract fun getChar(key: String): Char
    abstract fun getList(key: String?): List<Any>
    abstract fun getObject(key: String): AbstractVeric

    @Deprecated("")
    abstract fun getNull(key: String): Any?
    abstract fun <T> getRaw(key: String): VericElement<T>?

    abstract fun setInteger(key: String, value: Int)
    abstract fun setDouble(key: String, value: Double)
    abstract fun setFloat(key: String, value: Float)
    abstract fun setLong(key: String, value: Long)
    abstract fun setBoolean(key: String, value: Boolean)
    abstract fun setString(key: String, value: String?)
    abstract fun setChar(key: String, value: Char)
    abstract fun setRaw(key: String?, value: VericElement<*>)
    abstract fun setList(key: String, value: List<Any>)
    abstract fun setObject(key: String, value: AbstractVeric)

    @Deprecated("")
    abstract fun setNull(key: String)

    abstract fun keySet(): List<String>
    abstract fun has(key: String): Boolean
    abstract fun delete(key: String)

}
