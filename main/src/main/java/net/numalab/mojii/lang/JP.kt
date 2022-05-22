package net.numalab.mojii.lang

import java.awt.Font


class JP : LangSet {
    override fun langName(): String = "日本語"

    override fun langCode(): String {
        return "jp"
    }

    override fun chars(): CharSet {
        return JPCharSet()
    }
}

class JPCharSet : CharSet(
    "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわおんがぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽ".toCharArray()
        .map {
            FontDrawableChar(it, font)
        }
) {
    companion object {
        val font: Font = Font(Font.SANS_SERIF, Font.PLAIN, 128)
    }
}