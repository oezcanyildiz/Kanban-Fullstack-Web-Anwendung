# TeamSync – Collaborative Real-Time Kanban Board

**TeamSync** ist eine hochverfügbare Kollaborations-Plattform, die für die effiziente Teamarbeit in Echtzeit entwickelt wurde. Das Projekt dient der Vertiefung von Enterprise-Entwurfsmustern und der Bewältigung komplexer Backend-Herausforderungen in einer Java-Umgebung.

## 🚀 Kern-Features & Backend-Komplexität

Dieses Projekt fokussiert sich bewusst auf anspruchsvolle Backend-Logiken und geschäftskritische Funktionen:

* **Real-Time Architecture:** Implementierung einer ereignisgesteuerten Synchronisation mittels **Spring WebSockets (STOMP)**, um Datenkonflikte bei parallelen Zugriffen mehrerer Nutzer zu vermeiden.
* **Granular Security:** Entwurf eines detaillierten Berechtigungskonzepts (**Role-Based Access Control - RBAC**) mit **Spring Security**, das Zugriffsrechte dynamisch auf Workspace- und Board-Ebene validiert.
* **Automated Testing:** Absicherung der Business-Logic durch eine umfassende Test-Suite mit **JUnit 5 und Mockito**, um eine hohe Software-Qualität zu gewährleisten.

## 🛠 Tech-Stack

### Backend
* **Java 21:** Einsatz moderner Sprachfeatures.
* **Spring Boot 4.0.2:** Nutzung von Spring Data JPA, Spring Security und WebSockets.
* **PostgreSQL:** Relationales Datenbankdesign und performante Datenhaltung.
* **Hibernate / JPA:** Effizientes Object-Relational Mapping (ORM).

### DevOps & Tools
* **Docker:** Containerisierung der Anwendung für konsistente Deployment-Umgebungen.
* **Maven:** Professionelles Projekt- und Build-Management.
* **Git:** Strukturierte Versionsverwaltung.

## 🏗 Architektur

Die Anwendung folgt einer strikten **3-Tier-Architektur** (Controller, Service, Repository), um eine saubere Trennung von Geschäftslogik und Datenhaltung zu garantieren. Besonderer Wert wurde auf die Anwendung von Design Patterns und MVC-Strukturen gelegt.

## 🚦 Projektstatus

Das Projekt befindet sich aktuell in der aktiven Entwicklung (Backend-Fokus).
### * **Abgeschlossen:** Core-Backend-Struktur, relationales Datenbank-Schema, Sicherheitskonzept.
###* **In Arbeit:** Verfeinerung der Echtzeit-Synchronisation für großflächige Team-Workspaces.

---

## 💻 Installation & Start (Development)

1.  **Repository klonen:**
    ```bash
    git clone https://github.com/oezcanyildiz/Kanban-Fullstack-Web-Anwendung
    ```
2.  **Anwendung starten:**
    ```bash
    mvn spring-boot:run
    ```# Kanban-Fullstack-Web-Anwendung
Kanban Fullstack Web Anwendung
