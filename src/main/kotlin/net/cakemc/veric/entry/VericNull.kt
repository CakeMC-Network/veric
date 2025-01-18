package net.cakemc.veric.entry

class VericNull(private val key: String) : VericElement<Any?>() {
    override fun key(): String {
        return key
    }

    override fun type(): VericType {
        return VericType.NULL
    }

    override fun toFormatedVericString(): String {
        return "$key = null"
    }

    override fun value(): Any? {
        return null
    }
}
