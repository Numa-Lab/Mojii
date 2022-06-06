package net.numalab.mojii.judge

import net.numalab.mojii.map.MojiiMap
import org.bukkit.Location
import org.bukkit.entity.ItemFrame

class StringGetter {
    companion object {
        fun getFrom(from: Location): List<Pair<String, List<Pair<MojiiMap, ItemFrame>>>> {
            val maps = MojiiMapGetter.getFrom(from).map { it.getArr() }.flatten()
            return maps.map { l -> l.map { it.first.char.char }.joinToString(separator = "") to l }
        }
    }
}

/**
 * 先頭から一つずつ末尾のインデックスをずらしていったListを返す
 */
private fun <T> List<T>.getArr(): List<List<T>> {
    return indices.map {
        this.copy(0..it)
    }
}

private fun <T> List<T>.copy(intRange: IntRange): List<T> {
    val list = mutableListOf<T>()
    for (i in intRange) {
        list.add(this[i])
    }
    return list
}