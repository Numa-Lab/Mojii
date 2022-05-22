package net.numalab.mojii.map

import org.bukkit.entity.Player
import org.bukkit.map.MapCanvas
import org.bukkit.map.MapRenderer
import org.bukkit.map.MapView
import java.awt.image.BufferedImage

class MojiiMapRenderer(private val map: MojiiMap) : MapRenderer() {
    var img = BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB)

    override fun render(map: MapView, canvas: MapCanvas, player: Player) {
        // remove all pointers
        repeat(canvas.cursors.size()) {
            canvas.cursors.removeCursor(canvas.cursors.getCursor(0))
        }

        canvas.drawImage(0, 0, img)
    }
}