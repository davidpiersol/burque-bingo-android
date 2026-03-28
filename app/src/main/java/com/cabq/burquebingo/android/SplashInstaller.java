package com.cabq.burquebingo.android;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.core.splashscreen.SplashScreen;

/** Bridges SplashScreen.installSplashScreen for Kotlin (JVM static interop). */
public final class SplashInstaller {
    private SplashInstaller() {}

    public static void install(@NonNull Activity activity) {
        SplashScreen.installSplashScreen(activity);
    }
}
