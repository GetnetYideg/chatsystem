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
one to one chat ( private chat )
1. User logs in and opens a real-time WebSocket connection to the server.
2. User selects a friend and sends a message.
3. Server receives it and saves it to the database.
4. Server instantly routes the message privately to the friend's WebSocket.
5. The friend receives the message in real-time.
```
---
## Folder Structure
```bash
chatsystem/
│
├── docs/
│   └── database/
│       └── docs.md
│
├── src/
│   └── main/
│       ├── java/
│       │   ├── com/example/chatsystem/
│       │   │
│       │   │   ├── ChatClient.java
│       │   ├── ChatServer.java
│       │   ├── module-info.java
│       │
│       │   ├── client/
│       │   │   ├── controller/
│       │   │   │   ├── ChatController.java
│       │   │   │   ├── LoginController.java
│       │   │   │   └── RoomController.java
│       │   │   │
│       │   │   ├── models/
│       │   │   │   ├── message.java
│       │   │   │   └── userModel.java
│       │   │   │
│       │   │   ├── service/
│       │   │   │   ├── AuthClientService.java
│       │   │   │   └── ChatClientService.java
│       │   │   │
│       │   │   ├── ui/
│       │   │   │   ├── ChatScreen.java
│       │   │   │   ├── LoginScreen.java
│       │   │   │   └── RoomScreen.java
│       │   │   │
│       │   │   ├── util/
│       │   │   │   ├── Constants.java
│       │   │   │   └── jsonParser.java
│       │   │   │
│       │   │   └── websocket/
│       │   │       ├── clientWebSocket.java
│       │   │       └── messageListener.java
│       │   │
│       │   └── server/
│       │       ├── config/
│       │       │   ├── ServerConfig.java
│       │       │   └── webSocketconfig.java
│       │       │
│       │       ├── database/
│       │       │   ├── DBConnection.java
│       │       │   └── Databasemanager.java
│       │       │
│       │       ├── model/
│       │       │   ├── chatRoom.java
│       │       │   ├── messages.java
│       │       │   └── users.java
│       │       │
│       │       ├── repository/
│       │       │   ├── chatRoomRepository.java
│       │       │   ├── messageRepository.java
│       │       │   └── userRepository.java
│       │       │
│       │       ├── service/
│       │       │   ├── AuthService.java
│       │       │   ├── ChatService.java
│       │       │   └── UserService.java
│       │       │
│       │       └── websocket/
│       │           ├── ChatWebSocketHandler.java
│       │           ├── MessageRouter.java
│       │           └── SessionManager.java
│       │
│       └── resources/
│           ├── com/example/chatsystem/
│           │   └── hello-view.fxml
│           │
│           └── database/
│               ├── schema.sql
│               └── seed.sql
│
├── target/
│   └── classes/
│       └── com/example/chatsystem/
│           ├── client/
│           └── server/
│
├── .idea/
├── .mvn/
├── mvnw
├── mvnw.cmd
├── pom.xml
└── .gitignore
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
