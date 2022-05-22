package net.numalab.mojii.lang

import net.numalab.mojii.map.Drawer
import java.awt.image.BufferedImage

open class CharSet(val chars: List<DrawableChar>) {
    operator fun contains(c: Char): Boolean {
        return chars.any { it.char == c }
    }
}

abstract class DrawableChar(val char: Char) : Drawer {
    abstract override fun draw(img: BufferedImage): BufferedImage
}