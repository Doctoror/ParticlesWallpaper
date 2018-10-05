/*
 * Copyright (C) 2018 Yaroslav Mytkalyk
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
package com.doctoror.particleswallpaper.userprefs.multisampling

import android.app.Fragment
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.preference.ListPreference
import android.util.AttributeSet
import com.doctoror.particleswallpaper.R
import com.doctoror.particleswallpaper.presentation.di.components.AppComponentProvider
import com.doctoror.particleswallpaper.presentation.di.components.DaggerPreferenceComponent
import com.doctoror.particleswallpaper.presentation.preference.FragmentHolder
import javax.inject.Inject

class MultisamplingPreference @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : ListPreference(context, attrs), MultisamplingPreferenceView, LifecycleObserver, FragmentHolder {

    override var fragment: Fragment? = null

    @Inject
    lateinit var presenter: MultisamplingPreferencePresenter

    private var supported = true;

    init {
        DaggerPreferenceComponent.builder()
                .appComponent(AppComponentProvider.provideAppComponent(context))
                .build()
                .inject(this)

        isPersistent = false
        presenter.onTakeView(this)
        setOnPreferenceChangeListener { _, v ->
            presenter.onPreferenceChange(if (v == null) {
                0
            } else {
                (v as CharSequence).toString().toInt()
            })
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

    override fun setPreferenceSupported(supported: Boolean) {
        this.supported = supported
        applySummary()
        isEnabled = supported
    }

    override fun setValue(value: Int) {
        setValue(value.toString())
        applySummary()
    }

    override fun showRestartDialog() {
        fragment?.let {
            MultisamplingRestartDialog().show(it.fragmentManager, "MultisamplingRestartDialog")
        }
    }

    private fun applySummary() {
        summary = if (supported) entry else context.getText(R.string.Unsupported)
    }
}
