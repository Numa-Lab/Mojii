package net.numalab.mojii.map

import java.awt.Color
import java.awt.image.BufferedImage

/**
 * マップの背景を変えるヤツ
 */
class BackGroundDrawer(var color: Color) : Drawer {
    override fun draw(img: BufferedImage): BufferedImage {
        val g = img.graphics
        g.color = color
        g.fillRect(0, 0, img.width, img.height)
        g.dispose()
        return img
    }
}