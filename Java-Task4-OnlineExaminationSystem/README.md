# 🎓 Task 4 — Online Examination System

[![Java](https://img.shields.io/badge/-Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Swing](https://img.shields.io/badge/-Java%20Swing-4479A1?style=flat-square)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![Status](https://img.shields.io/badge/Status-Completed-brightgreen?style=flat-square)](#)

> Part of my **[OIBSIP](https://github.com/asad594/OIBSIP)** submissions — AICTE Oasis Infobyte Internship, Java Development Track.

---

## 📖 About

A desktop-based **Online Examination System** built with **Java Swing**, simulating a real exam-taking experience: user login, a timed multiple-choice test, and an instant, visual result summary.

---

## ✨ Features

- 🔐 **Login screen** — clean card-based UI with a security note
- ⏱️ **Live countdown timer** — turns to a warning color when time is running low
- 📊 **Question progress dots** — shows which question you're on at a glance
- 🖱️ **Interactive option cards** — selected answer highlighted with border + filled radio
- 🎯 **Result screen** — donut-chart style score indicator
- 📈 **Stats breakdown** — Correct / Incorrect / Time taken, plus a full answer review with check/cross icons
- 🎨 **Custom-painted UI** — rounded cards, color-coded badges, no default Swing look

---

## 🛠️ Tech Stack

| Component        | Technology                          |
|-------------------|--------------------------------------|
| Language          | Java                                 |
| UI Framework      | Java Swing (custom `Graphics2D` painting) |
| Timer             | `javax.swing.Timer`                  |
| Design            | Custom rounded panels, `Colors.java` constants class |

---

## 📂 Folder Structure

```
Java-Task4-OnlineExaminationSystem/
├── src/
│   ├── LoginPanel.java
│   ├── ExamPanel.java
│   ├── ResultPanel.java
│   ├── Colors.java
│   └── ...
├── screenshots/
│   ├── login.png
│   └── result.png
└── README.md
```

---

## 🖼️ Screenshots

| Login Screen | Exam Screen |
|:---:|:---:|
| ![Login Screen](screenshots/login.png) | ![Exam Screen](screenshots/exam.png) |

| Result Screen |
|:---:|
| ![Result Screen](screenshots/result.png) |

---

## ▶️ How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/asad594/OIBSIP.git
   ```
2. Navigate to this task's folder:
   ```bash
   cd OIBSIP/Java-Task4-OnlineExaminationSystem/src
   ```
3. Compile and run:
   ```bash
   javac *.java
   java Main
   ```

---

## 👤 Author

**Muhammad Asad** — Software Engineering Student, Bahria University Karachi
[GitHub](https://github.com/asad594) · [LinkedIn](https://www.linkedin.com/in/muhammadasad-arshad/)

---

### 🌟 Part of my OIBSIP Java Development Track journey
