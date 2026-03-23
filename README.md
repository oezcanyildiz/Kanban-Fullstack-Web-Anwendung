# TeamSync – Enterprise Workspace & Kanban Management

**TeamSync** ist eine umfassende Fullstack-Webanwendung zur effizienten Verwaltung von Organisationen, Teams und Aufgaben. Das Projekt wurde entwickelt, um fortgeschrittene Enterprise-Architekturmuster, sichere Authentifizierungsmechanismen und eine moderne React-Oberfläche in einem realen Anwendungsfall zu vereinen.

Dieses Projekt dient als Showcase für mein Backend-Engineering (Spring Boot 3, Java 21). Das Frontend wurde als modernes UI mittels "Vibecoding" (AI-unterstützte Programmierung durch Antigravity) und Tailwind CSS realisiert, um zu zeigen, wie ich als Backend-Spezialist vollständige Produkte mithilfe neuester KI-Werkzeuge iterativ umsetzen kann.

---

## 🚀 Kern-Features (Aktuelle Version V1)

* **Mandantenfähigkeit (Multi-Tenancy):** Benutzer sind strikt in Organisationen isoliert. Einladungen erfolgen über generierte Organisations-Codes.
* **Granulares RBAC (Role-Based Access Control):** Detailliertes Berechtigungskonzept mit den Rollen `ORG_ADMIN`, `TEAM_LEADER` und `USER`. Die Spring Security Konfiguration blockiert Zugriffe basierend auf der Hierarchie dynamisch auf Workspace- und Board-Ebene.
* **Zustandslose JWT-Authentifizierung:** Sichere Absicherung der REST-API mittels JSON Web Tokens, verarbeitet über maßgeschneiderte Security Filter-Chains.
* **API Security & Rate Limiting:** Serverseitiger DDoS-Schutz und gezieltes Rate-Limiting bestimmter Endpunkte mittels `Bucket4j`.
* **Interaktives Frontend (Vibecoding):** Eine vollständige, reaktionsschnelle Single-Page-Application (SPA), erbaut durch iteratives AI-Prompting und Prompt-Engineering.
* **Saubere Enterprise-Architektur:** Echte 3-Tier-Architektur (Controller, Service, Repository) im Backend, konsequente Nutzung von DTOs (Data Transfer Objects) zur Kapselung von Entitäten und ein `GlobalExceptionHandler` für konsistente Fehlerbehandlung.

---

## 🛠 Tech-Stack

### Backend
* **Java 21:** Nutzung moderner Sprachfeatures.
* **Spring Boot 3.x:** Fullstack-Backend mit Spring Data JPA, Spring Security und REST-Controllern.
* **JSON Web Tokens (JWT):** Sicherer, zustandsloser Authentifizierungsmechanismus (jjwt-Framework).
* **PostgreSQL:** Relationales Datenbankdesign mit Fokus auf Datenintegrität und referenziellen Constraints.
* **Hibernate / JPA:** Effizientes Object-Relational Mapping (ORM).
* **Bucket4j:** API Rate Limiting für sichere, produktionsreife Schnittstellen.

### Frontend (Vibecoding)
* **Tailwind CSS:** Modernes, responsives und sauberes UI-Design.
* **AI-Assisted Development:** Frontend primär per "Vibecoding" (Antigravity LLM Prompting) gebaut, gekoppelt mit meinem bestehenden Wissen in HTML5 & CSS3.

---

## 🗺️ Roadmap & Zukünftige Features (Version V2)

Für das nächste große Release (V2) ist der Umbau in eine vollständige **Real-Time Collaboration Platform** geplant. Die Vorbereitungen in der Architektur (`spring-boot-starter-websocket`) sind bereits getroffen:

1. **Secure Real-Time Chat (WebSockets):**
   - Implementierung eines Team-Chats über **Spring WebSockets (STOMP)**.
   - **JWT over WebSockets:** Handshake-Interceptors, die das Token bereits beim Verbindungsaufbau validieren.
2. **Live Board Synchronization:**
   - Verschiebungen im Kanban-Board sollen künftig direkt über den Message Broker an alle verbundenen Teammitglieder gepusht werden, um "Optimistic Locking Exceptions" bei parallelem Arbeiten zu vermeiden.
3. **Erweiterte Testabdeckung:**
   - Ausbau der automatisierten Backend-Tests (Unit- und Integrationstests) mit **JUnit 5, Mockito und Testcontainers**.
4. **CI/CD & Docker:**
   - Bereitstellung von isolierten `Dockerfiles` für Frontend und Backend zur Automatisierung des Deployments.

---

## 💻 Installation & Start (Docker Quickstart)

Du kannst die komplette Anwendung (Datenbank, Backend und Frontend) mit einem einzigen Befehl starten, ohne Java oder Node.js auf deinem Computer installieren zu müssen!

1. **Repository klonen:**
   ```bash
   git clone https://github.com/oezcanyildiz/Kanban-Fullstack-Web-Anwendung
   cd Kanban-Fullstack-Web-Anwendung
   ```

2. **Applikation vollautomatisch starten:**
   Stelle sicher, dass **Docker** (z.B. Docker Desktop) auf deinem PC läuft, und führe aus:
   ```bash
   docker compose up --build -d
   ```

3. **Applikation öffnen:**
   Falls du eine Domain (`boardly.one`) konfiguriert hast, regelt Caddy nun automatisch das SSL-Zertifikat.
   👉 **[https://boardly.one](https://boardly.one)**

   *Lokal ohne Domain erreichbar unter: [http://localhost:8081](http://localhost:8081) (falls Port 8081 gemappt ist).*

*Hinweis: Beim allerersten Start lädt Docker die Bilder (PostgreSQL, Java, Node, Nginx) herunter und baut das Projekt. Das kann wenige Minuten dauern.*
