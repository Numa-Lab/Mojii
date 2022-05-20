package net.numalab.mojii.lang

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
    "ABCDEFGHIJKLMNOPQRSTUVWXWZ".toCharArray().map { DrawableChar(it) }
)