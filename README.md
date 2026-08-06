# Gym Fitness Application -COMP2000 Coursework

An Android application supporting a gym booking scenario for two user roles.

--* Members* - browse trainers, book, cancel and modify sessions, view upcoming sessions, manage notification preferences, vie/edit account details

--* Trainers* - manage availability, view, cancel and modify bookings, and receive notification.

## Setup

1. Clone this respository
2. Open in Android Studio
3. Run on an emulator or physical device
4. Seeded test accounts: 'member1' / 'password123', 'trainer1' / 'password123'

## Key Features Implemented

- Local SQLite database (5 tables : users, sessions, availability, notifications, notifications_preferences ) via the DatabaseHelper
- Role-based authentication between member and trainer
- Full booking flow with time slot selection, database persistence and generated booking reference
- Cross-role session linking - bookings made be members appear on the trainer side.
- Cancel functionality for both members and trainers
- Account details view/edit for members
- Logout functionality for both roles
- Keyboard-accessible for login page
