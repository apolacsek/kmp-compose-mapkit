# Integrating MapKit in Compose Multiplatform Sample Project

This Kotlin Multiplatform project demonstrates MapKit integration for Android and iOS, serving as a companion to the blog post "Integrating MapKit in Compose Multiplatform".

## Prerequisites

### Development Environment

- Android Studio or IntelliJ IDEA
- Xcode 16 (for iOS development)
- Google Maps API Key

### Google Maps API Key Configuration

To run the project, you must provide a Google Maps API key:

1. Create a `local.properties` file in the project root directory (if it doesn't exist)
2. Add the following line to the file:
   ```
   mapsApiKey=YOUR_GOOGLE_MAPS_API_KEY
   ```
3. Replace `YOUR_GOOGLE_MAPS_API_KEY` with a valid Google Maps API key

### Getting Started

1. Clone the repository
2. Open the project in your preferred IDE
3. Add your Google Maps API key to `local.properties`
4. Sync the project and run on desired platforms

## Learning Resources

- [Kotlin Multiplatform Official Documentation](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform Guide](https://www.jetbrains.com/lp/compose-multiplatform/)

## Blog Post Reference

This sample project accompanies the blog post [Integrating MapKit in Compose Multiplatform](https://medium.com/@apolacsek/integrating-mapkit-in-compose-multiplatform-165de6141364). Refer to the associated blog post for detailed implementation insights and explanations.
