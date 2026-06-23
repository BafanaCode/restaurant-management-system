# 🍽️ Restaurant Management System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white)
![Status](https://img.shields.io/badge/Status-Academic_Project-lightgrey?style=for-the-badge)

An Android mobile application for managing restaurant operations. Built with Java and Android SDK, the app is designed to help restaurant staff manage orders or menu items through a mobile interface.

---

## 📋 Table of Contents

- [About the Project](#about-the-project)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [What I Learned](#what-i-learned)
- [Future Improvements](#future-improvements)

---

## About the Project

This project was built as an academic exercise in Android development. The application targets restaurant management workflows — such as managing menu items or handling orders — through a native Android interface. It uses Gradle as the build system and follows standard Android project conventions.

> 🎯 An introduction to Android development using Java and the Android SDK, applying UI and data management concepts to a restaurant context.

---

## ✨ Features

- 📋 View and manage restaurant menu items or orders
- 📱 Native Android mobile interface
- 🗂️ Structured app architecture following Android conventions

---

## 🛠️ Tech Stack

| Category    | Technology                  |
|-------------|-----------------------------|
| Language    | Java                        |
| Platform    | Android (Android SDK)       |
| Build Tool  | Gradle (Kotlin DSL)         |
| IDE         | Android Studio              |

---

## 📁 Project Structure

```
restaurant-management-system/
├── app/                      # Main Android app module
│   └── src/
│       ├── main/
│       │   ├── java/         # Java source files
│       │   ├── res/          # Layouts, drawables, strings
│       │   └── AndroidManifest.xml
│       └── test/             # Unit tests
├── gradle/                   # Gradle wrapper files
├── build.gradle.kts          # Project-level build config
├── settings.gradle.kts       # Project settings
└── .gitignore
```

---

## 🚀 Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (latest stable)
- Android SDK (API 21+ recommended)
- Java 11+

### Running the App

1. **Clone the repository**
   ```bash
   git clone https://github.com/BafanaCode/restaurant-management-system.git
   ```

2. **Open in Android Studio**
   - Select `File > Open` and navigate to the cloned folder

3. **Sync Gradle**
   - Android Studio will prompt you to sync Gradle — click `Sync Now`

4. **Run on emulator or device**
   - Select a device from the toolbar and press `Run ▶️`

---

## 📚 What I Learned

- Setting up and navigating an Android Studio project
- Building Android UIs using XML layouts and Java Activity classes
- Understanding the Android Activity lifecycle
- Using Gradle (Kotlin DSL) to manage dependencies and build configuration
- Applying Java OOP patterns (classes, methods, data models) in an Android context

---

## 🔮 Future Improvements

- [ ] Add a local database (Room/SQLite) to persist menu and order data
- [ ] Implement a proper order management workflow (place, update, complete)
- [ ] Add user roles — e.g. admin vs waiter views
- [ ] Improve UI with Material Design components

---

## 👨‍💻 Author

**Luyanda Nhlapho**
📍 Kimberley, South Africa
🎓 Advanced Diploma in ICT – Sol Plaatje University

[![GitHub](https://img.shields.io/badge/GitHub-BafanaCode-181717?style=flat-square&logo=github)](https://github.com/BafanaCode)
