package net.cakemc.veric.entry


class VericList(
    val elements: MutableList<VericElement<*>>,
    private val key: String
) : VericElement<MutableList<VericElement<*>>>() {

    override fun toFormatedVericString(): String {
        val elements = StringBuilder()
        for (element in this.elements) {
            elements.append(element.value()).append(" ")
        }
        return "$key = [\n$elements]"
    }

    override fun key(): String {
        return key
    }

    override fun type(): VericType {
        return VericType.LIST
    }

    override fun value(): MutableList<VericElement<*>> {
        return elements
    }
}
