package com.tompee.twitlet.dependency.module

import com.tompee.twitlet.core.image.Renderer
import com.tompee.twitlet.core.image.picasso.PicassoRenderer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ImageModule {
    @Singleton
    @Provides
    fun provideRenderer(renderer: PicassoRenderer): Renderer = renderer

    @Singleton
    @Provides
    fun providePicassoRenderer(): PicassoRenderer = PicassoRenderer()
}