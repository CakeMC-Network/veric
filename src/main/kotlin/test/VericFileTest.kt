package test

import net.cakemc.veric.Veric
import java.nio.file.Paths

fun main() {

    val veric = Veric()

    // defaults:
    veric.setString("string", "Hello, World!")
    veric.setInteger("integer", 100)
    veric.setDouble("double", 123.123)
    veric.setFloat("float", 12345F)
    veric.setLong("long", 111111111111111111L)
    veric.setChar("char", 'A')
    veric.setBoolean("boolean", true)

    // lists:
    val list = listOf("first", "last", "third")
    //veric.setList("list", list)

    // objects "endless" nesting possible (object in object in....)
    val complex = Veric()
    complex.setString("name", "Hi")
    complex.setInteger("age", 21)
    //complex.setList("activity", listOf("gaming", "music"))

    veric.setObject("object", complex)

    // save it to a file
    veric.saveToFile(Paths.get("./example.veric"))

    // load a Veric object from file
    val loaded = Veric.readFile(Paths.get("./example.veric"))

    // get defaults:
    println(veric.getString("string"))
    println(veric.getInteger("integer"))
    println(veric.getDouble("double"))
    println(veric.getFloat("float"))
    println(veric.getLong("long"))
    println(veric.getChar("char"))
    println(veric.getBoolean("boolean"))

    // lists:
    println(veric.getList("list"))

    // get the saved object and print it in the custom format
    println(loaded.getObject("object").toVericString())

    // contains check
    println(veric.has("double"))

    // printing all keys
    println(veric.keySet())

    // delete a key/value
    veric.delete("char")

    // print the whole object with formating
    println(veric.toVericString())
}
