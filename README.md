# Building
OneUI Tuner is built with the latest possible version of Android Studio, whether that be a Release Candidate, a Beta, or a Canary version.
Make sure you're using the latest version before building.

OneUI Tuner may also rely on some hidden APIs in Android. You can get a modified android.jar from [this repo](https://github.com/anggrayudi/android-hidden-api/), along with instructions on how to install it.
OneUI Tuner is currently built against Android 10 (API 29), so download the appropriate JAR.

There is also at least one local dependency in use. Check app/build.gradle and settings.gradle for instructions on replacing any local dependencies with remote references.