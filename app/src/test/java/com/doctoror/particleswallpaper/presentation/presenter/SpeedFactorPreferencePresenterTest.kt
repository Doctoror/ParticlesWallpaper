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
package com.doctoror.particleswallpaper.presentation.presenter

import com.doctoror.particleswallpaper.execution.TrampolineSchedulers
import com.doctoror.particleswallpaper.settings.MutableSettingsRepository
import com.doctoror.particleswallpaper.presentation.view.SeekBarPreferenceView
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.jupiter.api.Test

class SpeedFactorPreferencePresenterTest {

    private val settings: MutableSettingsRepository = mock()
    private val view: SeekBarPreferenceView = mock()

    private val underTest = SpeedFactorPreferencePresenter(TrampolineSchedulers(), settings)

    @Test
    fun testMapper() {
        testMapper(underTest)
    }

    @Test
    fun setsMaxValueOnTakeView() {
        // When
        underTest.onTakeView(view)

        // Then
        verify(view).setMaxInt(40)
    }

    @Test
    fun setsSpeedFactorOnStart() {
        // Given
        val speedFactor = 1.5f
        whenever(settings.getStepMultiplier()).thenReturn(Observable.just(speedFactor))
        underTest.onTakeView(view)

        // When
        underTest.onStart()

        // Then
        verify(view).setProgressInt(((speedFactor - 0.1f) * 10f).toInt())
    }

    @Test
    fun doesNotSetSpeedFactorAfterOnStop() {
        // Given
        val speedFactorSource = PublishSubject.create<Float>()
        whenever(settings.getStepMultiplier()).thenReturn(speedFactorSource)
        underTest.onTakeView(view)

        // When
        underTest.onStart()
        underTest.onStop()
        speedFactorSource.onNext(1f)

        // Then
        verify(view, never()).setProgressInt(any())
    }

    @Test
    fun storesSpeedFactorOnChange() {
        // Given
        val progress = 10

        // When
        underTest.onPreferenceChange(progress)

        // Then
        verify(settings).setStepMultiplier(progress.toFloat() / 10f + 0.1f)
    }
}
