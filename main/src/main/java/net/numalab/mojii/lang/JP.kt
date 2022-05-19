package net.numalab.mojii.lang


class JP : LangSet {
    override fun langName(): String = "日本語"

    override fun chars(): CharSet {
        return JPCharSet()
    }
}

class JPCharSet : CharSet(
    "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわおんがぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽ".toCharArray().map { DrawableChar(it) }
)