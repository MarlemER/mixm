package com.marlem.mixm.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.marlem.mixm.entities.Song
import com.marlem.mixm.utilities.Constants.SONG_COLLECTION
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class MusicDatabase {
    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(SONG_COLLECTION)

    suspend fun getAllSongs():List<Song>{
        return try {
            songCollection.get().await().toObjects(Song::class.java)//call the network actual que sera executada en la courtine, type list song
        }catch (e: Exception){
            emptyList()
        }
    }
}