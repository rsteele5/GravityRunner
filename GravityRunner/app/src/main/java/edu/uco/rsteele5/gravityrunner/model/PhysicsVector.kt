package edu.uco.rsteele5.gravityrunner.model

class PhysicsVector(var x: Float,var y: Float,var magnitude: Float) {
    constructor(): this(0f,0f,0f)

    fun getDeltaX(): Float{ return x*magnitude }
    fun getDeltaY(): Float{ return y*magnitude }

    fun add(vector: PhysicsVector): PhysicsVector{
        var nX = (x*magnitude) + (vector.x*vector.magnitude)
        var nY = (y*magnitude) + (vector.y*vector.magnitude)
        var nMag = Math.sqrt((nX*nX + nY*nY).toDouble()).toFloat()
        if(nMag == 0f) {
            nX = 0f
            nY = 0f
        }else{
            nX /= nMag
            nY /= nMag
        }
        return PhysicsVector(nX, nY, nMag)
    }

    fun zero(){
        x = 0f
        y = 0f
        magnitude = 0f
    }

}