package edu.uco.rsteele5.gravityrunner

import android.os.Parcel
import android.os.Parcelable

class LevelData(val level: Int, val score: Long, val coins: Int): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(level)
        parcel.writeLong(score)
        parcel.writeInt(coins)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LevelData> {
        override fun createFromParcel(parcel: Parcel): LevelData {
            return LevelData(parcel)
        }

        override fun newArray(size: Int): Array<LevelData?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Level: $level, Score: $score, Coins Collected: $coins"
    }
}