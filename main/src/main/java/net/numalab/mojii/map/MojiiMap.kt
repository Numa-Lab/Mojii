package net.numalab.mojii.map

import com.github.bun133.bukkitfly.stack.DelegatedItemStack
import com.github.bun133.bukkitfly.stack.DelegatedItemStack.Delegates.ItemMeta.mapMeta
import com.github.bun133.bukkitfly.stack.DelegatedItemStack.Delegates.persistentString
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta

class MojiiMap(stack: ItemStack) : DelegatedItemStack(stack) {
    companion object {
        private val key = NamespacedKey("mojii", "mapuuid")
    }

    init {
        if (!(stack.type == Material.MAP || stack.type == Material.FILLED_MAP)) {
            throw IllegalArgumentException("$stack is not a map")
        }
    }

    val mapMeta: MapMeta? by this.mapMeta()
    val mojiiUUID: String? by this.persistentString(key)
}

fun ItemStack.toMojiiMap(): MojiiMap? {
    return try {
        MojiiMap(this)
    } catch (e: IllegalArgumentException) {
        null
    }
}