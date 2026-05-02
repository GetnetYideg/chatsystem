# 💬 Chat App

🚀 A modern **real-time desktop chat application** built using a **pure Java backend**, **JavaFX UI**, and **WebSocket communication**, with persistent **MySQL storage**.

---

## ✨ Features

- 🔐 User Authentication (Login & Register)  
- 💬 Instant Messaging (Real-Time)  
- 📜 Chat History (Stored in Database)  
- 👥 Multi-user Support  
- 🔄 Live Message Updates  
- ⚡ Fast & Responsive UI  

---

 ## 🧠 Technologies Used

| Layer        | Technology |
|--------------|-----------|
| 🎨 Frontend  | JavaFX |
| ⚙️ Backend   | Pure Java |
| 🔌 Realtime  | WebSocket |
| 🗄️ Database | MySQL |
| 🔗 DB Access | JDBC |
---
## 🏗️ Architecture

```text
JavaFX Client
     ↓
WebSocket Connection
     ↓
Java Server (Pure Java)
     ↓
MySQL Database
```
---
## How it work ?
```text
1. User login
2. WebSocket connection starts
3. User sends message
4. Server:
   → Broadcasts message instantly to all connected clients
   → Saves message in database using JDBC
5. Receiver gets message in real-time
```
---
## Folder Structure
```bash
chat-app/
│
├── server/                    
│   ├── src/
│   │   └── com/chatapp/server/
│   │       ├── MainServer.java
│   │       ├── websocket/
│   │       ├── service/
│   │       ├── model/
│   │       └── database/
│
├── client/                    
│   ├── src/
│   │   └── com/chatapp/client/
│   │       ├── MainApp.java
│   │       ├── controller/
│   │       ├── ui/
│   │       └── websocket/
│
├── database/
│   └── schema.sql
│
└── README.md
```
---

## 🗄️ Database Schema

### 👤 Users Table

| Field    | Type     |
|----------|----------|
| id       | INT (PK) |
| username | VARCHAR  |
| password | VARCHAR  |

---

### 💬 Messages Table

| Field       | Type     |
|-------------|----------|
| id          | INT (PK) |
| sender_id   | INT      |
| receiver_id | INT      |
| message     | TEXT     |
| timestamp   | DATETIME |

## 🏁 Conclusion
```text
This project demonstrates a real-world chat system architecture using:

JavaFX for UI
WebSocket for real-time communication
MySQL with JDBC for storage
Pure Java backend for flexibility

It provides a solid foundation for building advanced real-time communication systems.
```
---
