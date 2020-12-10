package com.marlem.mixm.utilities

data class Resource<out T>(val status:Status,val data:T?,val message:String?) {
    companion object{
        //status
        fun <T> success(data:T?)=Resource(Status.SUCCESS,data,null)
        fun <T> error(message: String,data:T?) = Resource(Status.ERROR,data,message)
        //loading status, when come from cache while you load the data that comes from remote data source
        fun <T> loading(data:T?) = Resource(Status.LOADING,data,null)
    }
}

enum class Status{
    SUCCESS,
    ERROR,
    LOADING
}