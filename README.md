
# 🦁 Feed The Lion – Android Game App

**Feed The Lion** is a fun and interactive Android game developed as part of the course _Mobile Application Development_.

The objective is to move the lion using your phone's tilt sensor and collect as many meat pieces as possible while keeping track of your high scores and locations.

---

## 🎮 Main Features

- 🧭 Tilt-based control using the phone's accelerometer
- 🍖 Real-time gameplay and scoring system
- 🏆 **High Scores Screen**:
  - Displays the top 10 scores of all time
  - Connects each score to a map location
- 🗺️ **Google Maps Integration**:
  - Shows pins at the locations where high scores were achieved
  - Clicking on a score centers the map on the location
- 🔁 Restart button to return to the main screen from anywhere

---

## 📱 Technologies Used

- Android Studio
- Kotlin
- SharedPreferences (for persistent high score storage)
- Google Maps API
- RecyclerView
- Android Sensors (Accelerometer)

---

## 🧪 How to Run

1. Open the project in Android Studio
2. Add your Google Maps API key (if needed)
3. Run the app on a physical device or emulator

---

## 📁 Project Structure

- `MainActivity` – Game logic and lion control
- `StartActivity` – Launch screen
- `HighScoresActivity` – Displays scores and map
- `HighScoreFragment` – Shows score list
- `MapFragment` – Displays score locations on map
- `HighScoreManager` – Manages score saving and loading

---

## 👩‍💻 Developed By

**[Shani Shalev]**  
As part of the course: **Mobile Application Development**  
Academic Year: 2025
=======


