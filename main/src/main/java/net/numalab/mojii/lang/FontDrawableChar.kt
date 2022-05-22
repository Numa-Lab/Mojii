package net.numalab.mojii.lang

import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.image.BufferedImage

/**
 * Fontを使って文字を描画するクラス
 */
class FontDrawableChar(char: Char, val font: Font) : DrawableChar(char) {
    override fun draw(img: BufferedImage): BufferedImage {
        val g = img.graphics
        g.font = font
        val metrics = g.fontMetrics
        val s = "" + char
        val x = 64 - metrics.stringWidth(s) / 2
        val y = (128 - metrics.height) / 2 + metrics.ascent
        g.color = Color.BLACK
        g.drawString(s, x, y)
        return img
    }
}