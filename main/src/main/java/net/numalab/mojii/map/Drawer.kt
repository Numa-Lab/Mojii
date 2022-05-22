package net.numalab.mojii.map

import java.awt.image.BufferedImage

interface Drawer {
    fun draw(img: BufferedImage): BufferedImage
}