package com.pbh.androidbase.navigation

/** Resolved top-level destination the app should launch into, decided by the session guard. */
enum class StartRoute {
    /** User has no active session and should see login. */
    Login,

    /** User has an active session and should see home. */
    Home,
}
