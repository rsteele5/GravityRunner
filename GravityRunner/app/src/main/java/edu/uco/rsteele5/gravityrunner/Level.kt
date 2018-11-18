package edu.uco.rsteele5.gravityrunner

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude

class Level(var title:String, var src:String, var score:Int, var status:Int, var level: Int): Parcelable {

    constructor() : this("", "", 0, 0, 1)

    @get: Exclude
    var id: String=""

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(src)
        parcel.writeInt(score)
        parcel.writeInt(status)
        parcel.writeInt(level)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Level> {
        override fun createFromParcel(parcel: Parcel): Level {
            return Level(parcel)
        }

        override fun newArray(size: Int): Array<Level?> {
            return arrayOfNulls(size)
        }
    }
    override  fun equals(other: Any?): Boolean{
        if(other !is Level) return false
        return id == other.id
    }

    override fun toString(): String = "score = $score"
}