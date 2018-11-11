package edu.uco.rsteele5.gravityrunner

class Level(var title:String, var src:String, var score:Int?, var status:Int) {

    /*status: 1 -> cleared
              0 -> playing now
             -1 -> next stage, disable
     */
}