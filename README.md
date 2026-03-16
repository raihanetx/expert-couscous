# FaceUp - Video Calling App

A professional video calling Android application built with Kotlin and Jetpack Compose.

## Features

- **Phone + Password Authentication** - Secure login and registration
- **User Search** - Find users by phone number
- **Friend System** - Send, accept, and reject friend requests
- **One-on-One Video Calls** - WebRTC powered video calling
- **Push Notifications** - Firebase Cloud Messaging integration
- **Modern UI** - Material Design 3 with Dark Theme

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose with Material Design 3
- **Backend:** Supabase (Auth, Database, Realtime, Storage)
- **Push Notifications:** Firebase Cloud Messaging
- **Video Calling:** WebRTC
- **Architecture:** MVVM with Clean Architecture

## Requirements

- Android 8.0 (API 26) or higher
- Android Studio Hedgehog or later
- JDK 17

## Building the Project

### Prerequisites

1. Install Android Studio
2. Install JDK 17
3. Set up Android SDK (API 34)

### Build Steps

```bash
# Clone the repository
git clone https://github.com/raihanetx/expert-couscous.git
cd expert-couscous

# Grant execute permission
chmod +x gradlew

# Build Debug APK
./gradlew assembleDebug

# Build Release APK
./gradlew assembleRelease
```

## Configuration

### Supabase Setup

1. Create a Supabase project at https://supabase.com
2. Update `SupabaseClient.kt` with your Supabase URL and Anon Key
3. Run the SQL migrations from `supabase/migrations/` in the Supabase SQL editor

### Firebase Setup

1. Create a Firebase project
2. Add your `google-services.json` to `app/` directory
3. Enable Cloud Messaging in Firebase Console

## Project Structure

```
app/src/main/java/com/evenly/faceup/
├── MainActivity.kt           # Main entry point
├── data/                     # Data layer
│   ├── local/               # Local data sources
│   ├── remote/              # Remote data sources
│   └── repository/          # Repositories
├── domain/                   # Business logic
│   ├── model/               # Domain models
│   └── usecase/             # Use cases
├── ui/                       # UI layer
│   ├── screens/             # Compose screens
│   ├── navigation/          # Navigation setup
│   ├── theme/               # App theming
│   └── components/          # Reusable components
└── di/                       # Dependency injection
```

## Screenshots

*Screenshots will be added soon*

## License

This project is proprietary software. All rights reserved.

## Contributing

This is a private project. Contact the maintainers for contribution guidelines.
