package com.marlem.mixm.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.request.RequestOptions
import com.marlem.mixm.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)//donde estalaremos el modulo durante el lifecycle de la app
object AppModule {

    @Singleton
    @Provides
    fun provideGlideInstance(
        //parameters
    @ApplicationContext context: Context
    ) = Glide.with(context)
        .setDefaultRequestOptions(RequestOptions()
            .placeholder(R.drawable.exo_controls_fastforward)
            .error(R.drawable.exo_icon_play)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )
}