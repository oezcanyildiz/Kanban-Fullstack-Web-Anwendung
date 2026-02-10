# TeamSync – Collaborative Real-Time Kanban Board

**TeamSync** ist eine hochverfügbare Kollaborations-Plattform, die für die effiziente Teamarbeit in Echtzeit entwickelt wurde. Das Projekt dient der Vertiefung von Enterprise-Entwurfsmustern und der Bewältigung komplexer Backend-Herausforderungen in einer Java-Umgebung.

## 🚀 Kern-Features & Backend-Komplexität

Dieses Projekt fokussiert sich auf fortgeschrittene Architektur-Muster, insbesondere die Absicherung von Echtzeit-Kommunikation:

* **Secure Real-Time Architecture:** Implementierung einer ereignisgesteuerten Synchronisation mittels **Spring WebSockets (STOMP)**. Besonderer Fokus liegt auf der **Zustandsverwaltung** und der Vermeidung von Race Conditions bei parallelen Zugriffen.
* **JWT over WebSockets:** Da Standard-WebSocket-Verbindungen keine Header wie klassische REST-Calls unterstützen, wurde eine sichere Lösung zur **Token-Validierung beim Handshake und Channel-Interception** implementiert, um JWT-basierte Authentifizierung auch im Echtzeit-Stream zu gewährleisten.
* **Granular Security (RBAC):** Ein detailliertes Berechtigungskonzept mit **Spring Security**, das Zugriffsrechte dynamisch auf Workspace- und Board-Ebene validiert, bevor Nachrichten über den Message Broker verteilt werden.
* **Automated Testing:** Absicherung der geschäftskritischen Logik durch eine umfassende Test-Suite mit **JUnit 5 und Mockito**.
## 🛠 Tech-Stack

### Backend
* **Java 21:** Nutzung moderner Sprachfeatures und funktionaler Programmierung.
* **Spring Boot 4.0.2:** Fullstack-Backend mit Spring Data JPA, Spring Security und WebSockets.
* **JSON Web Tokens (JWT):** Sicherer, zustandsloser Authentifizierungsmechanismus.
* **PostgreSQL:** Relationales Datenbankdesign mit Fokus auf Datenintegrität.
* **Hibernate / JPA:** Effizientes Object-Relational Mapping (ORM).

### DevOps & Tools
* **Docker:** Containerisierung der Anwendung für konsistente Deployment-Umgebungen.
* **Maven:** Professionelles Projekt- und Build-Management.
* **Git:** Strukturierte Versionsverwaltung.

## 🏗 Architektur

Die Anwendung folgt einer strikten **3-Tier-Architektur** (Controller, Service, Repository), um eine saubere Trennung von Geschäftslogik und Datenhaltung zu garantieren. Besonderer Wert wurde auf die Anwendung von Design Patterns und MVC-Strukturen gelegt.
## 🚦 Projektstatus

Das Projekt befindet sich aktuell in der aktiven Entwicklung (Backend-Fokus).
### * **Abgeschlossen:** Core-Backend, relationales Datenbank-Schema, JWT-Infrastruktur.
### * **In Arbeit:** Optimierung der WebSocket-Interceptors für die Token-Validierung und Real-Time-Synchronisation.

---

## 💻 Installation & Start (Development)

1.  **Repository klonen:**
    ```bash
    git clone https://github.com/oezcanyildiz/Kanban-Fullstack-Web-Anwendung
    ```
2.  **Anwendung starten:**
    ```bash
    mvn spring-boot:run
    ```https://github.com/oezcanyildiz/Kanban-Fullstack-Web-Anwendung# Kanban-Fullstack-Web-Anwendung
Kanban Fullstack Web Anwendung
