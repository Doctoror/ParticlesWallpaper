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
package com.doctoror.particleswallpaper.userprefs.optimizetextures

import com.bumptech.glide.Glide
import com.doctoror.particleswallpaper.framework.execution.TrampolineSchedulers
import com.doctoror.particleswallpaper.userprefs.data.SettingsRepositoryOpenGL
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.jupiter.api.Test

class OptimizeTexturesPreferencePresenterTest {

    private val glide: Glide = mock()
    private val settings: SettingsRepositoryOpenGL = mock()
    private val view: OptimizeTexturesView = mock()

    private val underTest = OptimizeTexturesPreferencePresenter(
            glide,
            settings,
            TrampolineSchedulers()
    ).apply {
        onTakeView(view)
    }

    @Test
    fun setsCheckedStateBasedOnSettingsOnStart() {
        // Given
        whenever(settings.observeOptimizeTextures()).thenReturn(Observable.just(true))

        // When
        underTest.onStart()

        // Then
        verify(view).setChecked(true)
    }

    @Test
    fun storesPreferenceChangeValueInSettings() {
        // When
        underTest.onValueChanged(true)

        // Then
        verify(settings).optimizeTextures = true
    }

    @Test
    fun clearsGlideCacheOnValueChange() {
        // When
        underTest.onValueChanged(false)

        // Then
        verify(glide).clearMemory()
    }
}
