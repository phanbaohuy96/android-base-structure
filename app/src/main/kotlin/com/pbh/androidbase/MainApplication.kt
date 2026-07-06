package com.pbh.androidbase

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/** Application entry point that bootstraps Hilt for the template. */
@HiltAndroidApp
class MainApplication : Application()
