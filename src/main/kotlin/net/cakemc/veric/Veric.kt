package net.cakemc.veric

import net.cakemc.veric.entry.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class Veric(
    name: String,
    inner: MutableList<VericElement<*>>
) : AbstractVeric(name, inner) {

    companion object {

        fun readFromBytes(byteArray: ByteArray): AbstractVeric {
            val lexer = LexerTokenizer.parse(byteArray)
            val stream = TokenStream.wrap(lexer)
            val parser = VericParser(stream)

            return parser.parse()
        }

        fun fromVericString(value: String): AbstractVeric {
            return readFromBytes(value.toByteArray(Charsets.UTF_8))
        }

        fun readFile(path: Path): AbstractVeric {
            return this.fromVericString(Files.readString(path))
        }

    }

    constructor() : this("root__", ArrayList())
    constructor(name: String) : this(name, ArrayList())

    override fun getInteger(key: String): Int {
        val element: VericElement<*> = getRaw<Any>(key)
        check(element.type() != VericType.NULL) { "key: $key is not present in context!" }

        if (element is VericInt) {
            return element.value()
        } else throw IllegalStateException(
            "invalid type for key: $key expected: '${VericType.INT}' but got: '${element.key()}'"
        )
    }

    override fun getDouble(key: String): Double {
        val element: VericElement<*> = getRaw<Any>(key)
        check(element.type() != VericType.NULL) { "key: $key is not present in context!" }

        if (element is VericDouble) {
            return element.value()
        } else throw IllegalStateException(
            "invalid type for key: $key expected: '${VericType.DOUBLE}' but got: '${element.key()}'"
        )
    }

    override fun getFloat(key: String): Float {
        val element: VericElement<*> = getRaw<Any>(key)
        check(element.type() != VericType.NULL) { "key: $key is not present in context!" }

        if (element is VericFloat) {
            return element.value()
        } else throw IllegalStateException(
            "invalid type for key: $key expected: '${VericType.FLOAT}' but got: '${element.key()}'"
        )
    }

    override fun getLong(key: String): Long {
        val element: VericElement<*> = getRaw<Any>(key)
        check(element.type() != VericType.NULL) { "key: $key is not present in context!" }

        if (element is VericLong) {
            return element.value()
        } else throw IllegalStateException(
            "invalid type for key: $key expected: '${VericType.LONG}' but got: '${element.key()}'"
        )
    }

    override fun getBoolean(key: String): Boolean {
        val element: VericElement<*> = getRaw<Any>(key)
        check(element.type() != VericType.NULL) { "key: $key is not present in context!" }

        if (element is VericBoolean) {
            return element.value()
        } else throw IllegalStateException(
            "invalid type for key: $key expected: '${VericType.BOOLEAN}' but got: '${element.key()}'"
        )
    }

    override fun getString(key: String): String {
        val element: VericElement<*> = getRaw<Any>(key)
        check(element.type() != VericType.NULL) { "key: $key is not present in context!" }

        if (element is VericString) {
            return element.value()
        } else throw IllegalStateException(
            "invalid type for key: $key expected: '${VericType.STRING}' but got: '${element.key()}'"
        )
    }

    override fun getChar(key: String): Char {
        val element: VericElement<*> = getRaw<Any>(key)
        check(element.type() != VericType.NULL) { "key: $key is not present in context!" }

        if (element is VericCharacter) {
            return element.value()
        } else throw IllegalStateException(
            "invalid type for key: $key expected: '${VericType.CHAR}' but got: '${element.key()}'"
        )
    }

    override fun <T> getRaw(key: String): VericElement<T> {
        return elements.stream().filter { element: VericElement<*> ->
            (element.key()
                    == key)
        }.findFirst().orElse(null) as VericElement<T>
    }

    override fun getList(key: String?): List<Any> {
        return listOf()
    }

    override fun getObject(key: String): AbstractVeric {
        val element: VericElement<*> = getRaw<Any>(key)
        check(element.type() != VericType.NULL) { "key: $key is not present in context!" }

        if (element is VericObject) {
            return element.value()
        } else throw IllegalStateException(
            "invalid type for key: $key expected: '${VericType.OBJECT}' but got: '${element.key()}'"
        )
    }

    @Deprecated("")
    override fun getNull(key: String): Any? {
        val element: VericElement<*> = getRaw<Any>(key)
        check(element.type() != VericType.NULL) { "key: $key is not present in context!" }

        if (element is VericNull) {
            return element.value()
        } else throw IllegalStateException(
            "invalid type for key: $key expected: '${VericType.NULL}' but got: '${element.key()}'"
        )
    }

    override fun has(key: String): Boolean {
        return elements.stream()
            .anyMatch { vericElement: VericElement<*> -> vericElement.key() == key }
    }

    override fun delete(key: String) {
        elements.removeIf { vericElement: VericElement<*> -> vericElement.key() == key }
    }

    override fun keySet(): List<String> {
        return elements.stream().map { obj: VericElement<*> -> obj.key() }.toList()
    }

    override fun setInteger(key: String, value: Int) {
        elements.add(VericInt(key, value.toString()))
    }

    override fun setDouble(key: String, value: Double) {
        elements.add(VericDouble(key, value.toString()))
    }

    override fun setFloat(key: String, value: Float) {
        elements.add(VericFloat(key, value.toString()))
    }

    override fun setLong(key: String, value: Long) {
        elements.add(VericLong(key, value.toString()))
    }

    override fun setBoolean(key: String, value: Boolean) {
        elements.add(VericBoolean(key, value.toString()))
    }

    override fun setString(key: String, value: String?) {
        elements.add(VericString(key, "\"$value\""))
    }

    override fun setChar(key: String, value: Char) {
        elements.add(VericCharacter(key, value.toString()))
    }

    override fun setRaw(key: String?, value: VericElement<*>) {
        elements.add(value)
    }

    override fun setList(key: String, value: List<Any>) {
        elements.add(VericList(value, key))
    }

    override fun setObject(key: String, value: AbstractVeric) {
        elements.add(VericObject(key, value))
    }

    @Deprecated("")
    override fun setNull(key: String) {
        elements.add(VericNull(key))
    }

    override fun toVericString(): String {
        return VericObject(this.key, this).toFormatedVericString()
    }

    override fun toBytes(): ByteArray {
        return toVericString().toByteArray(Charsets.UTF_8)
    }

    override fun readFromBytes(byteArray: ByteArray): AbstractVeric {
        val lexer = LexerTokenizer.parse(byteArray)
        val stream = TokenStream.wrap(lexer)
        val parser = VericParser(stream)

        return parser.parse()
    }

    override fun fromVericString(value: String): AbstractVeric {
        return readFromBytes(value.toByteArray(Charsets.UTF_8))
    }

    override fun readFile(path: Path): AbstractVeric {
        return this.fromVericString(Files.readString(path))
    }

    override fun saveToFile(path: Path) {
        val string = this.toVericString()

        Files.createDirectories(path.parent)
        Files.writeString(
            path, string, StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        )
    }

}
