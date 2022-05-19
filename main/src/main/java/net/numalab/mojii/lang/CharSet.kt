package net.numalab.mojii.lang

import net.numalab.mojii.map.MojiiMap

class CharSet(val chars: List<LangedChar>) {
    operator fun contains(c: Char): Boolean {
        return chars.any { it.char == c }
    }
}

class LangedChar(val char: Char) {
    fun drawTo(map: MojiiMap) {
        // TODO 文字の描画
    }
}