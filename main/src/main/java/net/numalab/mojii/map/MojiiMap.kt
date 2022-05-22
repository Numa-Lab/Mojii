package net.numalab.mojii.map

import com.github.bun133.bukkitfly.stack.DelegatedItemStack
import com.github.bun133.bukkitfly.stack.DelegatedItemStack.Delegates.ItemMeta.mapMeta
import net.numalab.mojii.lang.DrawableChar
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta
import kotlin.reflect.KProperty

class MojiiMap(stack: ItemStack, var char: DrawableChar) : DelegatedItemStack(stack) {
    init {
        if (!(stack.type == Material.MAP || stack.type == Material.FILLED_MAP)) {
            throw IllegalArgumentException("$stack is not a map")
        }

        // Add to instance list
        stacks.add(this)
    }

    val mapMeta: MapMeta? by this.mapMeta()
    val mapRenderer: MojiiMapRenderer? by Delegate.MojiiRenderer(this)
    val mapDrawer: MojiiMapDrawer = MojiiMapDrawer(this)

    object Delegate {
        class MojiiRenderer(val map: MojiiMap) {
            operator fun getValue(map: MojiiMap, property: KProperty<*>): MojiiMapRenderer? {
                val meta = map.mapMeta ?: return null
                val view = meta.mapView ?: return null
                val f = view.renderers.filterIsInstance(MojiiMapRenderer::class.java)
                return if (f.isEmpty()) {
                    val renderer = MojiiMapRenderer(map)
                    view.addRenderer(renderer)
                    renderer
                } else {
                    f[0]
                }
            }
        }
    }
}

private val stacks = mutableListOf<MojiiMap>()
fun ItemStack.toMojiiMap(): MojiiMap? {
    return stacks.find { it.stack == this }
}

fun genMojiiMap(char: DrawableChar): MojiiMap {
    val stack = ItemStack(Material.FILLED_MAP)
    return MojiiMap(stack, char)
}