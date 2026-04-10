# TeamSync — Vorgehensweise & Verbesserungsplan

## Phase 1 — Bugs fixen

### Schritt 1.1 — HTTP 429 Status Code fix (10 Min)
**Datei:** `src/main/java/com/yildiz/teamsync/exceptions/GlobalExceptionHandler.java`

- Zeile ~51: `HttpStatus.NOT_FOUND` → `HttpStatus.TOO_MANY_REQUESTS`
- `RateLimitExceededException` vom `config`-Package ins `exceptions`-Package verschieben

**Warum:** Rate Limiting gibt aktuell 404 zurück. Clients denken, die URL existiert nicht, anstatt zu wissen dass sie zu viele Requests schicken.

---

### Schritt 1.2 — Raw RuntimeExceptions ersetzen (30 Min)
**Dateien:**
- `src/main/java/com/yildiz/teamsync/services/impl/BoardService.java` — Zeile 206 & 258
- `src/main/java/com/yildiz/teamsync/security/SecurityUtils.java` — Zeile 34 & 37

Alle `throw new RuntimeException(...)` durch passende Custom Exceptions ersetzen:
- `RuntimeException("Zugriff verweigert")` → `AccessDeniedException`
- `RuntimeException("Board nicht gefunden")` → `ResourceNotFoundException`
- `RuntimeException("Kein authentifizierter Benutzer")` → `UnauthorizedException`

**Warum:** Raw RuntimeException umgeht den GlobalExceptionHandler und gibt dem Client immer HTTP 500, egal was der eigentliche Fehler war.

---

### Schritt 1.3 — Assignee Auth-Bypass fixen (45 Min)
**Datei:** `src/main/java/com/yildiz/teamsync/services/impl/TaskService.java` — Zeile 122–126

**Problem:** Bei Task-Update kann `assigneeID` auf jeden beliebigen User in der DB gesetzt werden, auch aus anderen Organisationen. Es wird nur geprüft ob der User existiert, nicht ob er im Team ist.

**Fix:** Nach `userRepository.findById(...)` prüfen ob der Assignee Mitglied des Teams ist:
```java
boolean assigneeIsMember = teamMemberRepository.existsByTeam_TeamIDAndUser_UserID(
    task.getBoardColumn().getBoard().getTeam().getTeamID(),
    assigneeUser.getUserID()
);
boolean assigneeIsOwner = task.getBoardColumn().getBoard().getTeam().getOwner()
    .getUserID().equals(assigneeUser.getUserID());

if (!assigneeIsMember && !assigneeIsOwner) {
    throw new BadRequestException("Der zugewiesene Benutzer gehört nicht zu diesem Team.");
}
```

**Warum:** Das ist ein IDOR-Vulnerability (Insecure Direct Object Reference) — OWASP Top 10.

---

### Schritt 1.4 — N+1 Queries fixen (1–2 Std)
**Datei:** `src/main/java/com/yildiz/teamsync/services/impl/BoardService.java`

**Problem A — `getMyBoards` (Zeile 232):**
```java
// Aktuell: Lädt ALLE Boards aus der DB, filtert dann in Java
boardRepository.findAll().stream().filter(...)

// Fix: Methode existiert bereits im Repository!
boardRepository.findByTeam_TeamIDInAndDeletedFalse(teamIDs)
```

**Problem B — `getBoardDetails`:**
Für ein Board mit N Spalten und M Tasks werden aktuell 1 + N + N*M Queries gefeuert (N+1 Problem). Fix: Custom JPQL Query mit `JOIN FETCH` in `BoardTaskRepository`:
```java
@Query("SELECT t FROM BoardTask t " +
       "JOIN FETCH t.boardColumn " +
       "LEFT JOIN FETCH t.assignee " +
       "WHERE t.boardColumn.board.boardID = :boardID " +
       "AND t.deleted = false " +
       "ORDER BY t.boardColumn.columnPosition ASC, t.position ASC")
List<BoardTask> findAllByBoardIDWithDetails(@Param("boardID") Long boardID);
```
Dann alle Tasks für das Board in einem Query laden, nach Spalten-ID gruppieren (`Collectors.groupingBy`), und in der Schleife aus der Map lesen statt Repository aufzurufen.

---

## Phase 2 — Tests schreiben

**Reihenfolge:**

| # | Testklasse | Framework | Was gelernt wird |
|---|---|---|---|
| 1 | `TaskServiceTest` | Mockito + JUnit5 | Unit Tests, Mock-Repositories, Isolation |
| 2 | `GlobalExceptionHandlerTest` | MockMvc + @WebMvcTest | Web-Slice-Tests, HTTP Status Codes |
| 3 | `BoardTaskRepositoryTest` | @DataJpaTest + H2 | Repository-Tests, Custom Queries testen |
| 4 | `TaskIntegrationTest` | @SpringBootTest + Testcontainers | Full-Stack Test mit echter PostgreSQL |

**Wichtig:** Tests erst nach Phase 1 schreiben. Wer erst testet, bevor er versteht was korrektes Verhalten ist, schreibt Tests die Bugs einzementieren.

---

## Phase 3 — WebSocket (Real-time Task Updates)

> `spring-boot-starter-websocket` ist bereits in `pom.xml` — keine neue Dependency nötig!

**Schritte:**

1. `WebSocketConfig.java` erstellen
   - STOMP Endpoint: `/ws`
   - Broadcast Prefix: `/topic`
   - App Prefix: `/app`
   - SockJS Fallback aktivieren

2. `WebSocketAuthInterceptor.java` erstellen
   - JWT aus dem STOMP `CONNECT` Frame Header validieren
   - Wichtig: Nach dem HTTP Handshake ist der normale `Authorization` Header nicht mehr verfügbar
   - Bestehende `JwtUtils.java` wiederverwenden

3. `TaskEventPublisher.java` erstellen
   - `SimpMessagingTemplate` injizieren
   - Nach `taskRepository.save()` in `TaskService` an `/topic/boards/{boardID}/tasks` publishen
   - In `createTask`, `updateTask`, `deleteTask` aufrufen

4. (Optional) Team Chat
   - `ChatMessage` Entity, `@MessageMapping` Controller
   - `/topic/boards/{boardID}/chat` als Destination

---

## Phase 4 — CI/CD mit GitHub Actions

> Erst sinnvoll, wenn Phase 2 (Tests) abgeschlossen ist.

**`.github/workflows/ci.yml`** mit zwei Jobs:

**Job 1 — Tests** (bei jedem Push/PR auf `main`):
- Java 21 setup
- `./mvnw test` ausführen
- Testcontainers funktioniert auf GitHub Actions automatisch (Docker vorhanden)

**Job 2 — Docker Build** (nur bei Merge auf `main`):
- `./mvnw package -DskipTests`
- Docker Image bauen
- Optional: Push zu Docker Hub oder GitHub Container Registry

---

## Fortschritt

- [ ] Phase 1.1 — HTTP 429 Fix
- [ ] Phase 1.2 — RuntimeExceptions ersetzen
- [ ] Phase 1.3 — Assignee Auth-Bypass fixen
- [ ] Phase 1.4 — N+1 Queries fixen
- [ ] Phase 2 — Tests schreiben
- [ ] Phase 3 — WebSocket
- [ ] Phase 4 — CI/CD

---

*Erstellt am 08.04.2026*
