# Fan Remote 🌬️

An Android application that turns your smartphone into a remote control for your fan. This app uses your device's built-in Infrared (IR) blaster to send signals, allowing you to control power, speed, swing, and other functions directly from your phone.

The UI is built with Jetpack Compose, featuring a modern, dark theme for a clean and intuitive user experience.

---

## ⚠️ Important Warning

**This application is specifically designed for the *Maspion MWF 3601 RC* fan model.**

Button functions and infrared (IR) signals may **not be compatible** or may work differently with other fan models or brands. Use on unsupported devices may lead to unexpected behavior.

---

## ✨ Features

* **Status Display**: A clean card interface shows the current fan status (Speed, Timer, Swing, Wind Mode).
* **Power Control**: Turn the fan ON and OFF.
* **Speed Adjustment**: Cycle through fan speeds (Low, Mid, High).
* **Swing Control**: Toggle the oscillation feature on and off.
* **Timer Function**: Set a timer to automatically turn off the fan (0.5h, 1h, 2h, 4h).
* **Wind Mode**: Switch between different wind modes (Normal, Rhythm, Sleep).
* **Hardware Check**: Automatically detects if your device has an IR blaster on startup.

---

## 📱 Requirements

* An Android smartphone or tablet.
* A built-in **Infrared (IR) Blaster**. This is essential for the app to function.

---

## 🛠️ Tech Stack

* **Language**: Kotlin
* **UI Toolkit**: Jetpack Compose
* **Core API**: Android `ConsumerIrManager` for IR transmission.

---

## 🚀 Installation

To build and run this project from the source code:
1.  Clone this repository.
2.  Open the project in Android Studio.
3.  Build the project and run it on an Android device equipped with an IR blaster.

---

## 👨‍💻 Created By

* FahryIbra
