package net.numalab.mojii.lang

import java.awt.Font

class EN : LangSet {
    override fun langName(): String {
        return "English"
    }

    override fun langCode(): String {
        return "en"
    }

    override fun chars(): CharSet {
        return ENCharSet()
    }
}

class ENCharSet : CharSet(
    "ABCDEFGHIJKLMNOPQRSTUVWXWZ".toCharArray().map { FontDrawableChar(it, font) },
) {
    companion object {
        val font: Font = Font(Font.SANS_SERIF, Font.PLAIN, 128)
    }
}