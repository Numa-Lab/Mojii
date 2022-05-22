package net.numalab.mojii.map

import java.awt.image.BufferedImage

class CharDrawer(var drawer: MojiiMap) : Drawer {
    override fun draw(img: BufferedImage): BufferedImage {
        val c = drawer.char
        // TODO Draw char
    }
}