package net.numalab.mojii.lang

import java.awt.Font
import java.awt.image.BufferedImage

/**
 * Fontを使って文字を描画するクラス
 */
class FontDrawableChar(char: Char, val font: Font) : DrawableChar(char) {
    override fun draw(img: BufferedImage): BufferedImage {
        val g = img.graphics
        g.font = font
        g.drawString(char.toString(), 0, 0)
        return img
    }
}