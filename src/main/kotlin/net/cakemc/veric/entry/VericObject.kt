package net.cakemc.veric.entry

import net.cakemc.veric.AbstractVeric
import net.cakemc.veric.Veric

class VericObject : VericElement<AbstractVeric> {
    private val veric: AbstractVeric
    private val key: String

    constructor(key: String, elements: MutableList<VericElement<*>>) {
        this.veric = Veric(key, elements)
        this.key = key
    }

    constructor(key: String, veric: AbstractVeric) {
        this.veric = veric
        this.key = key;
    }

    override fun key(): String {
        return veric.key
    }

    override fun type(): VericType {
        return VericType.OBJECT
    }

    override fun toFormatedVericString(): String {
        val builder = StringBuilder()
        for (element in veric.elements) {
            builder.append(element.toFormatedVericString()).append("\n")
        }

        if (key == "root__") {
            return builder.toString()
        }

        return "${key} = {\n$builder}"
    }

    override fun value(): AbstractVeric {
        return veric
    }
}
