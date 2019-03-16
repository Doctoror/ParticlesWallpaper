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
package com.doctoror.particleswallpaper.userprefs.enginetips

import android.app.AlertDialog
import android.content.Context
import android.preference.Preference
import android.util.AttributeSet
import com.doctoror.particleswallpaper.R
import com.doctoror.particleswallpaper.framework.di.inject
import org.koin.core.parameter.parametersOf

class EngineTipsPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : Preference(context, attrs), EngineTipsPreferenceView {

    private val presenter: EngineTipsPreferencePresenter by inject(
        context = context,
        parameters = { parametersOf(this as EngineTipsPreferenceView) }
    )

    init {
        isPersistent = false
    }

    override fun onClick() {
        presenter.onClick()
    }

    override fun showDialog() {
        AlertDialog
            .Builder(context)
            .setTitle(title)
            .setMessage(R.string.engine_tips)
            .setPositiveButton(R.string.Close, null)
            .show()
    }
}
