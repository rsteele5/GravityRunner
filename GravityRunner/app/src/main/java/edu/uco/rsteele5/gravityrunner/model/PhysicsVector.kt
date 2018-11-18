package edu.uco.rsteele5.gravityrunner.model

class PhysicsVector(var x: Float,var y: Float,var magnitude: Float) {
    constructor(): this(0f,0f,0f)

    fun getDeltaX(): Float{ return x*magnitude }
    fun getDeltaY(): Float{ return y*magnitude }

    fun zero(){
        x = 0f
        y = 0f
        magnitude = 0f
    }

    fun add(vector: PhysicsVector): PhysicsVector{
        val nX = getDeltaX() + vector.getDeltaX()
        val nY = getDeltaY() + vector.getDeltaY()
        val components = getNewDirectionAndMag(nX, nY)
        return PhysicsVector(components.first, components.second, components.third)
    }

    fun average(vector: PhysicsVector): PhysicsVector{
        val nX = (getDeltaX() + vector.getDeltaX())/2f
        val nY = (getDeltaY() + vector.getDeltaY())/2f
        val components = getNewDirectionAndMag(nX, nY)
        return PhysicsVector(components.first, components.second, components.third)
    }

    fun approximateOpposite(vector: PhysicsVector): Boolean{
        val comparable = this.add(vector)
        comparable.x = (Math.round(comparable.x * 100.0) / 100.0).toFloat()
        comparable.y = (Math.round(comparable.y * 100.0) / 100.0).toFloat()

        return (comparable.x < 0.005f && comparable.y < 0.005f)
    }

    fun deprecateMagnitudeBy(value: Float){
        if(magnitude - value <= 0f)
            magnitude = 0f
        else
            magnitude -= value
    }
    fun incrementMagnitudeUpTo(i: Float, max: Float){
        if(magnitude + i >= max)
            magnitude = max
        else
            magnitude += i
    }

    fun compareDirection(vector: PhysicsVector): Boolean{
        return x == vector.x && y == vector.y
    }

    /* Parameters
     * nX: the x component of a vector
     * nY: the y component of a vector
     * Output
     * Triple(x direction, y direction, Magnitude)
     */
    private fun getNewDirectionAndMag(nX: Float, nY: Float): Triple<Float,Float,Float>{
        val nMag = Math.sqrt((nX*nX + nY*nY).toDouble()).toFloat()
        return if(nMag == 0f)
            Triple(0f,0f,0f)
        else
            Triple(nX/nMag, nY/nMag, nMag)
    }
}