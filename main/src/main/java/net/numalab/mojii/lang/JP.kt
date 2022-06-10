package net.numalab.mojii.lang

import net.numalab.mojii.Mojii
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
            FontDrawableChar(it, Mojii.font)
        }
)