package net.numalab.mojii.judge

import net.numalab.mojii.map.MojiiMap
import org.bukkit.Location
import org.bukkit.entity.ItemFrame

class StringGetter {
    companion object {
        fun getFrom(from: Location): List<Pair<String, List<Pair<MojiiMap, ItemFrame>>>> {
            val maps = MojiiMapGetter.getFrom(from)
            return maps.map { l -> l.map { it.first.char.char }.joinToString(separator = "") to l }
        }
    }
}