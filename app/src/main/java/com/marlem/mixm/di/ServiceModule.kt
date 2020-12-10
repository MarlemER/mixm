package com.marlem.mixm.di

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.marlem.mixm.remote.MusicDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

//need our service lifetime
@Module
@InstallIn(ServiceComponent::class)//installin dependiendo lo que necesitemos fragment, activity
object ServiceModule {
    @ServiceScoped
    @Provides
    fun provideMusicDatabase() = MusicDatabase()//inject musicaDatabase in FirebaseMusicSource

    @ServiceScoped//serviceComponent es similar a un singleton por eso se pone esto
    @Provides
    fun provideAudioAttributes() = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @ServiceScoped
    @Provides
    fun provideExoPlayer(
        @ApplicationContext context:Context,
        audioAttributes:AudioAttributes) = SimpleExoPlayer.Builder(context).build().apply {//apply para setear el audioAttribute a la funcion
        setAudioAttributes(audioAttributes,true)
        setHandleAudioBecomingNoisy(true)//pause music player in heardphones
    }

    @ServiceScoped
    @Provides//function para firebase connect to db
    fun provideDataSourceFactory(@ApplicationContext context: Context) =
        DefaultDataSourceFactory(context, Util.getUserAgent(context,"Mixm App"))
}