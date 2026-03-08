## TicTacToe-Server

A real-time multiplayer Tic-Tac-Toe backend service with user authentication and game management.
Written in Java and Spring Boot, built with Gradle, and containerized with Docker.

---

## Tech Stack

- **Language:** Java 21
- **Build Tool:** Gradle 8.14.2
- **Frameworks:** Spring Boot 3.5.3, Spring Security, Spring Data JPA, Spring WebSocket
- **Database:** MySQL
- **Others:**
  - Hibernate
  - MapStruct
  - JWT (Access + Refresh Token)
  - WebSocket (STOMP) Messaging
  - JUnit 5 + Mockito for testing
  - Lombok

---

## Description

The project implements a complete backend infrastructure for a multiplayer Tic-Tac-Toe game with REST API and WebSocket support:
- User registration and authentication with JWT tokens
- Game creation, invitations, and management
- Real-time game play via WebSocket (STOMP)
- Player statistics and game history
- Graceful disconnect handling with automatic game cancellation
- ELO rating system

---

## Features

- ✅ **JWT Auth (Access + Refresh tokens)**
- ✅ **User registration and authentication**
- ✅ **Game invitations and propositions**
- ✅ **Real-time game play via STOMP/WebSocket**
- ✅ **Move validation and game logic**
- ✅ **Player rating system (ELO-based)**
- ✅ **Automatic disconnect handling**
- ✅ **Game history and statistics**
- ✅ **Exception handling with detailed error messages**
- ✅ **Comprehensive unit tests**

---

## API Endpoints

### Authentication
- `POST /api/v1/auth/signin` - User sign in
- `POST /api/v1/auth/signup` - User registration
- `POST /api/v1/auth/refresh` - Refresh JWT token

### Games
- `GET /api/v1/games/` - Get all games for current user
- `GET /api/v1/games/{id}` - Get specific game details
- `GET /api/v1/games/board/{id}` - Get game board state
- `POST /api/v1/games/` - Create new game
- `DELETE /api/v1/games/{id}` - Cancel game

### Players
- `GET /api/v1/players/{id}` - Get player profile
- `GET /api/v1/players/{id}/games` - Get player's game history

### WebSocket
- **Endpoint:** `/ws/connect`
- **Message mapping:** `/app/game/move` - Send move during game

---

## Quick Start

### 1. Prerequisites
- Docker

### 2. Local Development
```bash
./gradlew bootRun
```

---

## Configuration

Key settings in `src/main/resources/application.yaml`:

```yaml
game:
  rating_increase: 25
  acceptable_disconnect_time: 20000  # 20 seconds

security:
  jwt:
    lifetime: 900000  # 15 minutes
```

---

## Project Structure

```
src/main/java/com/tictactoe/server/
├── config/          # Spring configuration
├── controllers/     # REST and WebSocket endpoints
├── core/           # Game logic and session management
├── dto/            # Data transfer objects
├── enums/          # Game enums (status, coordinates)
├── exceptions/     # Custom exceptions
├── mappers/        # MapStruct mappers
├── models/         # JPA entities
├── repositories/   # Data access layer
├── security/       # JWT and security configuration
├── services/       # Business logic
└── utils/          # Utility classes
```

---

## License

This project is developed for educational purposes. All rights reserved.
