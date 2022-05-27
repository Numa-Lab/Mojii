package net.numalab.mojii.judge

import org.bukkit.Location

class StringGetter {
    companion object {
        fun getFrom(from: Location): List<String> {
            val maps = MojiiMapGetter.getFrom(from)
            return maps.map { l -> l.map { it.char.char }.joinToString(separator = "") }
        }
    }
}