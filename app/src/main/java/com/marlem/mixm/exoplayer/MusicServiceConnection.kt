package com.marlem.mixm.exoplayer

import android.content.ComponentName
import android.content.Context
import android.media.browse.MediaBrowser
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.MediaController
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marlem.mixm.utilities.Constants.NETWORK_ERROR
import com.marlem.mixm.utilities.Event
import com.marlem.mixm.utilities.Resource

//communication between activity or fragment and our service
class MusicServiceConnection (context: Context){
    //Livedata object apply en VM contain the information from our service and that observe to update in fragment
    private val _isConnected = MutableLiveData<Event<Resource<Boolean>>>()//uderscore guion bajo
    val isConnected:LiveData<Event<Resource<Boolean>>> = _isConnected

    private val _networkError = MutableLiveData<Event<Resource<Boolean>>>()
    val networkError:LiveData<Event<Resource<Boolean>>> = _networkError
    //state of playMusic
    private val _playbackState = MutableLiveData<PlaybackStateCompat?>()
    val playbackState:LiveData<PlaybackStateCompat?> = _playbackState
    //information about the song
    private val _curPlayingSong = MutableLiveData<MediaMetadataCompat?>()
    val curPlayingSong:LiveData<MediaMetadataCompat?> = _curPlayingSong
    //controllers example song pause, skip the next song, stuff all callback that we register
    lateinit var  mediaController: MediaControllerCompat
    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)
    private val mediaBrowser=MediaBrowserCompat(context, ComponentName(context,MusicService::class.java),mediaBrowserConnectionCallback,null)
        .apply {
            connect()
        }
    //override because not yet instantiate this media controller object though the session token of the service and we only have access through callback in the service
    val transportControls:MediaControllerCompat.TransportControls
            get() = mediaController.transportControls

    fun suscribe(parentId:String,callback:MediaBrowserCompat.SubscriptionCallback){
        mediaBrowser.subscribe(parentId,callback)
    }

    fun unsuscribe(parentId:String,callback: MediaBrowserCompat.SubscriptionCallback){
        mediaBrowser.unsubscribe(parentId,callback)
    }

    //callback for media browser
    private inner class MediaBrowserConnectionCallback(private val context: Context):MediaBrowserCompat.ConnectionCallback(){
        //override function
        //one time service is connecting is active, we need the token connection of mediaController
        override fun onConnected() {
            //token trough create mediaBrowser instance first
            mediaController = MediaControllerCompat(context,mediaBrowser.sessionToken).apply {
                registerCallback(MediaControllerCallback())
            }
            _isConnected.postValue(Event(Resource.success(true)))
        }

        override fun onConnectionSuspended() {
            _isConnected.postValue(Event(Resource.error("The connection was suspended",false)))
        }

        override fun onConnectionFailed() {
            _isConnected.postValue(Event(Resource.error("Couldnt connect to media browser",false)))
        }
    }

    //override the functions
    private inner class MediaControllerCallback:MediaControllerCompat.Callback(){
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            //observer on that from our fragment
            _playbackState.postValue(state)
        }
        //override the actual metadata play songs
        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            _curPlayingSong.postValue(metadata)
        }
        //session event to send custom event from our service to this connection callback in notify the error
        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
            when(event){
                NETWORK_ERROR -> _networkError.postValue(Event(Resource.error("Couldnt connect to the server. Please check your internet connection.", null)))
            }
        }
        //when the session is destroyed
        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }

    }

}