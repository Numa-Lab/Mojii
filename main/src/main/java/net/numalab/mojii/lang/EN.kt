package net.numalab.mojii.lang

import net.numalab.mojii.Mojii

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
    "ABCDEFGHIJKLMNOPQRSTUVWXWZ".toCharArray().map { FontDrawableChar(it, Mojii.font) },
)