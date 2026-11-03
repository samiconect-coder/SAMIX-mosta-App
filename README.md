# SAMIX mosta App 🇩🇿

An Arabic-first Morse-code learning app with a modern Algerian Casbah zellige-inspired visual identity.

## First version
- Morse alphabet A-Z and digits 0-9
- Text-to-Morse conversion
- Morse-to-text decoding
- Morse audio playback
- Basic knowledge quiz
- Offline operation

## Build APK
The GitHub Actions workflow in `.github/workflows/android.yml` builds a debug APK on every push to `main`, or when manually started from the Actions tab.

After a successful run:
1. Open the repository's **Actions** tab.
2. Open the latest **Build SAMIX mosta App APK** run.
3. Download the `SAMIX-mosta-App-debug` artifact.
4. Extract the ZIP to get `app-debug.apk`.

Package: `com.samix.mosta`
Minimum Android: 6.0 (API 23)
Target SDK: 30
