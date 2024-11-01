# Should I Stay or Should I Go (SISSI)

SISSI is an Android application that provides personalized pub/bar recommendations based on users' length of stay and visit history. The system calculates recommendations by analyzing the current location and previously visited places.

## Features

- Real-time GPS location tracking
- Google Maps integration for navigation
- Personalized pub recommendations
- Rating system for visited pubs
- Detailed pub information including descriptions and photos
- Multi-language support

## Technical Stack

### Android Development
- Kotlin as primary language
- MVVM Architecture
- Android Jetpack components
- Data Binding
- LiveData & ViewModel
- Fragment-based UI

### Location Services
- Google Maps API
- Google Location Services
- GPS tracking
- Geofencing

### Networking
- Fuel HTTP Client
- RESTful API integration
- JSON serialization/deserialization
- Kotlinx Serialization

### UI/UX
- Material Design components
- Custom tab navigation
- Dynamic layouts
- Image loading with Glide

### Testing
- JUnit for unit testing
- Mockk for Kotlin mocking
- Android Instrumentation tests

### Build Tools
- Gradle
- Android SDK 29
- AndroidX libraries

## Demo

See [SISSI Android Demo](https://www.youtube.com/watch?v=Del5X4rqogw&ab_channel=egochen)

## Requirements

- Android 6.0 (API 23) or higher
- Google Play Services
- Internet connection
- GPS enabled device
