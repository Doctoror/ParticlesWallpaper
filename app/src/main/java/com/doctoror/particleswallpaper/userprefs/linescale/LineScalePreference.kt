/*
 * Copyright (C) 2017 Yaroslav Mytkalyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.doctoror.particleswallpaper.userprefs.linescale

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.util.AttributeSet
import com.doctoror.particleswallpaper.framework.di.components.AppComponentProvider
import com.doctoror.particleswallpaper.framework.di.components.DaggerPreferenceComponent
import com.doctoror.particleswallpaper.framework.preference.SeekBarPreference
import com.doctoror.particleswallpaper.framework.preference.SeekBarPreferenceView
import javax.inject.Inject

/**
 * Created by Yaroslav Mytkalyk on 30.05.17.
 *
 * The line scale preference.
 */
class LineScalePreference @JvmOverloads constructor
(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : SeekBarPreference(context, attrs, defStyle),
        SeekBarPreferenceView,
        LifecycleObserver {

    @Inject
    lateinit var presenter: LineScalePreferencePresenter

    init {
        DaggerPreferenceComponent.builder()
                .appComponent(AppComponentProvider.provideAppComponent(context))
                .build()
                .inject(this)

        isPersistent = false
        presenter.onTakeView(this)
        setOnPreferenceChangeListener { _, v ->
            presenter.onPreferenceChange(v as Int?)
            true
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        presenter.onStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        presenter.onStop()
    }

    override fun setMaxInt(max: Int) {
        this.max = max
    }

    override fun setProgressInt(progress: Int) {
        this.progress = progress
    }

    override fun getMaxInt() = max
}
