package net.cakemc.veric.entry

abstract class VericElement<Value> {
    abstract fun key(): String
    abstract fun type(): VericType

    abstract fun value(): Value

    abstract fun toFormatedVericString(): String
}
