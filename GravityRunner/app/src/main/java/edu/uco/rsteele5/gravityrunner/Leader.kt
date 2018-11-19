package edu.uco.rsteele5.gravityrunner

import android.os.Parcel
import android.os.Parcelable

class Leader(var name: String, var score: Int) : Parcelable {
    constructor() : this("", 0)

    var id: String = ""

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    ) {
        id = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(score)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Leader> {
        override fun createFromParcel(parcel: Parcel): Leader {
            return Leader(parcel)
        }

        override fun newArray(size: Int): Array<Leader?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Leader) return false
        return id == other.id
    }

    override fun toString(): String = "Name: $name,     Score: $score "
}