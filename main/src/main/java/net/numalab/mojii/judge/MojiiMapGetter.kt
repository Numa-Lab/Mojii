package net.numalab.mojii.judge

import net.numalab.mojii.map.MojiiMap
import net.numalab.mojii.map.toMojiiMap
import org.bukkit.Location
import org.bukkit.entity.ItemFrame

class MojiiMapGetter {
    companion object {
        /**
         * 指定Locationから、全方向にMojiiMapを取得し続ける
         */
        fun getFrom(from: Location): List<List<Pair<MojiiMap, ItemFrame>>> {
            val frames = ItemFrameGetter.getFrom(from)
            return frames.map { l ->
                l.map {
                    val e = it.item.toMojiiMap()
                    if (e != null) e to it
                    else null
                }.beforeFirstNull()
            }
        }
    }
}

private fun <T : Any> List<T?>.beforeFirstNull(): List<T> {
    return when (val firstNullIndex = indexOfFirst { it == null }) {
        -1 -> {
            this.filterNotNull()
        }
        0 -> {
            emptyList()
        }
        else -> {
            this.subList(0, firstNullIndex).filterNotNull()
        }
    }
}