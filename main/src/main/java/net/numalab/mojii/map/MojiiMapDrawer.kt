package net.numalab.mojii.map

import java.awt.Color
import java.awt.image.BufferedImage

class MojiiMapDrawer(val map: MojiiMap) {
    val background = BackGroundDrawer(Color.WHITE)
    val charDrawer = CharDrawer(map)

    /**
     * 再描画
     */
    fun redraw() {
        val r = map.mapRenderer
        if (r != null) {
            redraw(r)
        }
    }

    fun redraw(r: MojiiMapRenderer) {
        var img = BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB)
        // 背景描画
        img = background.draw(img)
        // 文字描画
        img = charDrawer.draw(img)
        // 画像を更新
        r.img = img
    }
}