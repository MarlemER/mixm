package com.marlem.mixm.utilities

//handled the event
open class Event<out T>(private val data:T) {//open for inhert
    var hasBeenHandled = false
    //trigger for specific event a single time
    private set
    fun getContentIfNotHandled():T?{
        return if(hasBeenHandled){
            null
        }else{
            hasBeenHandled = true
            data
        }
    }

    fun peekContent() = data
}
