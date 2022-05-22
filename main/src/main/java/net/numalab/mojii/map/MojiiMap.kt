package net.numalab.mojii.map

import com.github.bun133.bukkitfly.component.text
import com.github.bun133.bukkitfly.stack.DelegatedItemStack
import com.github.bun133.bukkitfly.stack.DelegatedItemStack.Delegates.ItemMeta.mapMeta
import net.numalab.mojii.lang.DrawableChar
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.World
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

    fun redraw() {
        mapDrawer.redraw()
    }

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

val stacks = mutableListOf<MojiiMap>()
fun ItemStack.toMojiiMap(): MojiiMap? {
    return stacks.find { it.stack == this }
}

fun genMojiiMap(char: DrawableChar, world: World): MojiiMap {
    val map = Bukkit.getServer().createMap(world)
    map.renderers.toList().forEach {
        map.removeRenderer(it)
    }

    val stack = ItemStack(Material.FILLED_MAP)
    stack.editMeta {
        it.displayName(text("" + char.char))
        it as MapMeta
        it.mapView = map
    }
    return MojiiMap(stack, char)
}