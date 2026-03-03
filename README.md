# 🍎 AeroApple - Motion-Controlled Catcher Game

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-orange.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-blue.svg)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Firebase-Integrated-yellow.svg)](https://firebase.google.com)

**AeroApple** là một trò chơi di động được xây dựng bằng công nghệ Android hiện đại. Người chơi điều khiển chiếc giỏ bằng cách nghiêng điện thoại để hứng những quả táo và vật phẩm đang rơi.

---

## 📸 Screenshots

| Main Menu | Gameplay | Game Over | Store | High Score | 
|:---:|:---:|:---:|:---:|:---:|
| <img width="108" height="240" alt="Screenshot_20260303_201612" src="https://github.com/user-attachments/assets/58d1d2c6-b0b1-430d-ae2f-7542edc4eb34" /> | <img width="108" height="240" alt="Screenshot_20260303_201638" src="https://github.com/user-attachments/assets/dfb2c8b5-83f6-4b49-b98e-f1eb72b13dc0" /> <img width="108" height="240" alt="Screenshot_20260303_201645" src="https://github.com/user-attachments/assets/c7f79dd3-1b90-4aa8-a9e4-7a9db569758f" /> | <img width="108" height="240" alt="Screenshot_20260303_201706" src="https://github.com/user-attachments/assets/694d46c8-6133-4dd3-aa51-228680f21d9b" /> | <img width="108" height="240" alt="Screenshot_20260303_201859" src="https://github.com/user-attachments/assets/afe87a8a-10c2-45a8-92d3-f7f6dd09aa05" /> <img width="108" height="240" alt="Screenshot_20260303_201850" src="https://github.com/user-attachments/assets/f2b6c667-a814-462a-9cb0-9b5925c61623" /> | <img width="108" height="240" alt="Screenshot_20260303_201832" src="https://github.com/user-attachments/assets/fb2fdcce-f287-4c43-ad73-61818eb7370b" />

---

## 🚀 Tính năng chính

### 🕹️ Cơ chế Gameplay (Physics & Sensors)
* **Cảm biến trọng trường**: Sử dụng `SensorEventListener` với `TYPE_ACCELEROMETER` để điều khiển giỏ di chuyển mượt mà theo độ nghiêng điện thoại.
* **Custom Canvas Drawing**: Vẽ toàn bộ Game World (Táo, Bom, Coin) bằng `Canvas` trong Compose, tối ưu hóa hiệu năng render.
* **Collision Detection**: Thuật toán tính toán va chạm thời gian thực giữa giỏ và các vật thể rơi để cập nhật điểm và máu.

### 🏪 Hệ thống Cửa hàng (Basket Store)
* **Cloud Sync**: Dữ liệu vật phẩm (Baskets) được đồng bộ từ **Firebase Firestore**.
* **Offline Storage**: Sử dụng **Room Database** để lưu trữ các vật phẩm đã sở hữu, đảm bảo trải nghiệm không bị gián đoạn.
* **Reactive UI**: Luồng dữ liệu nhất quán giữa **DataStore** (ID đang chọn) và Room giúp giao diện tự động cập nhật khi đổi giỏ.

---

## 🛠 Công nghệ sử dụng (Tech Stack)

* **Ngôn ngữ**: Kotlin
* **UI**: Jetpack Compose với **Material 3**
* **Kiến trúc**: MVVM (Clean Architecture)
* **Dependency Injection**: Koin
* **Database**: Room, DataStore, Firebase Firestore
* **Monitor**: Firebase Analytics & Crashlytics
* **Khác**: Navigation 3, Coil, Sensor API

---

## 🏗 Cấu trúc Project

```text
app/src/main/java/com/example/easygame
├── data/           
│    ├── local/          # Databases (Room, DataStore)
│    ├── remote/         # Firebase
│    └── repository/     # Repositories
├── di/             # Koin Modules (App/ViewModel modules)
├── domain/         # Collision logic & Sensor managers
├── navigation/     # Navigation Graph
├── ui/             
│    ├── common/         # Reusable UI (BalanceBar, ErrorDialog, CustomButtons)
│    ├── screen/         # HomeScreen, GameScreen, StoreScreen, Leaderboard
│    └── theme/          # Theme, Color, Type, Dimen
├── Activity
└── Application
```



## 📝 Liên hệ
* **Author**: Nguyễn Tiến Đạt
* **Email**: dat1207.tiennguyen@gmail.com
* **Project**: AeroApple - Một dự án học tập về Game Development trên Android.
