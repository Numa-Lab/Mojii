package net.numalab.mojii.lang

import net.numalab.mojii.map.MojiiMap

open class CharSet(val chars: List<DrawableChar>) {
    operator fun contains(c: Char): Boolean {
        return chars.any { it.char == c }
    }
}

class DrawableChar(val char: Char) {
    fun drawTo(map: MojiiMap) {
        // TODO 文字の描画
    }
}