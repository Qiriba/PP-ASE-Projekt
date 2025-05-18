# 1 Domain Driven Design

## Analyse der Ubiquitous Language

Die Ubiquitous Language ist eine einheitliche Sprache, die von Entwicklern und Domänenexperten gleichermaßen verwendet wird. Sie sorgt dafür, dass alle Beteiligten dieselben Begriffe in denselben Kontexten verstehen.

In diesem Projekt wurden domänenspezifische Begriffe klar identifiziert und in den Code überführt:

| Begriff (Fachlich)           | Technische Umsetzung               |
|-----------------------------|------------------------------------|
| Konto                       | `Account` (Entity)                 |
| Kunde                       | `Customer` (Entity)                |
| Transaktion                 | `Transactions` (Entity)            |
| KontoNr                     | `AccountNumber` (Value Object)     |
| Guthaben                    | `Money` (Value Object)             |
| Überweisung / Einzahlung    | `TransactionType` (Enum)           |
| Authentifizierung           | `PIN`, `Password`, `JwtProvider`   |

Diese Begriffe wurden konsistent verwendet, sowohl im Code (z. B. Methodennamen, Klassennamen) als auch in den DTOs, Exceptions und Repositories.

---

## Analyse und Begründung der verwendeten taktischen DDD-Muster

### Entities

- `Customer`, `Account` und `Transactions` sind als Entities modelliert.
- Sie besitzen jeweils eine Identität (`id`, `accountNumber`) und verändern über die Zeit ihren Zustand (z. B. Kontostand).
- Die Gleichheit richtet sich nach der Identität, nicht nach den Attributwerten.

### Value Objects

- Unveränderlich, keine Identität:
    - `AccountNumber`, `Money`, `Password`, `PIN`
- Dienen der Modellierung domänenspezifischer Werte mit starker Typisierung, Validierung und Methodenlogik (z. B. `Money.add()`, `PIN.isValid()`).
- Beispiel: `Money` prüft innerhalb des Value Objects selbst auf Gültigkeit und erlaubt z. B. Addition, ohne dass Rohwerte manipuliert werden.

### Aggregates

- Aggregate definieren eine Konsistenzgrenze.
- Aggregate Roots:
    - `Account` ist Root des Account-Aggregats → kapselt `Transactions`, `Money`, `AccountNumber`
    - `Customer` ist Root des Customer-Aggregats
- Nur über die Root darf Zugriff auf abhängige Objekte erfolgen – dies wurde eingehalten.

### Repositories

- Technische Implementierungen:  
  `SpringAccountRepositoryAdapter`, `SpringCustomerRepositoryAdapter`, `SpringTransactionRepositoryAdapter`
- Schnittstellen definiert im `application.ports.out`-Paket: z. B. `AccountRepositoryPort`
- Zweck: Entkoppelung der Infrastruktur (JPA/DB) von der Domäne

### Domain Services

- Beispiel: `TransactionService`, `AccountCreationService`
- Modellieren Domänenlogik, die nicht natürlich zu einer einzelnen Entity gehört (z. B. kontoübergreifende Transfers)
- Vermeiden anämische Modelle, da sie Methoden aufrufen, die die Entities selbst ausführen (`account.deposit()` etc.)


# 2 Clean Architecture

## Architekturwahl: Hexagonale Architektur

Im Projekt wurde bewusst die hexagonale Architektur gewählt, um eine klare Trennung zwischen der Geschäftslogik (Kern) und der technischen Infrastruktur zu erreichen. Diese Architekturform bietet gegenüber einer klassischen Schichtarchitektur den Vorteil, dass die Domänenlogik vollständig unabhängig von Frameworks und technischen Details bleibt. Der Anwendungskern ist somit stabil, testbar und erweiterbar, unabhängig davon wie Daten gespeichert oder Benutzereingaben verarbeitet werden.

Die hexagonale Architektur folgt dem Prinzip, dass die Domäne über Ports (Interfaces) mit der Außenwelt kommuniziert. Die konkreten Implementierungen dieser Ports, sogenannte Adapter, befinden sich außerhalb des Kerns. Dadurch lässt sich technische Infrastruktur leicht austauschen, ohne die Geschäftslogik anzupassen.

Die Projektstruktur ist konsequent entlang dieser Architektur aufgebaut:

- **`3-domain` (Kern der Anwendung)**  
  Enthält alle zentralen **Domänenmodelle**, **Value Objects** und die **Geschäftslogik**.  
  Klassen wie `Account`, `Transactions`, `Money` oder `AccountNumber` sind hier angesiedelt und bilden die fachliche Grundlage des Systems.

- **`2-application` (Application Services und Ports)**  
  Stellt die **Use Cases** der Anwendung über Interfaces (Ports) bereit, wie `AccountCreationUseCase` oder `TransactionUseCase`.  
  Die **Services** implementieren diese Ports und orchestrieren den Zugriff auf die Domänenmodelle.

- **`1-adapters` (technische Adapter: Controller, Persistence, Security)**  
  Enthält Adapter für konkrete technische Details:
    - **Controller** in `interfaces.controller` übernehmen HTTP-Anfragen.
    - **Repositories** in `persistence` implementieren die Outbound-Ports mit z. B. Spring Data.
    - **Security-Komponenten** wie `JwtFilter` und `SecurityConfig` befinden sich in `security`.

- **`0-plugins` (Spring Boot Einstiegspunkt und Konfiguration)**  
  Enthält den Anwendungseinstieg (`BankingApplication.java`) und die zentrale Konfigurationsdateien.

Diese Architektur ermöglicht eine gerichtete Abhängigkeit:  
Nur die äußeren Schichten kennen die inneren, aber niemals umgekehrt. So bleibt die Domäne isoliert, unabhängig und leicht testbar.

---

# 3 Programming Principles

## Anwendung objektorientierter Prinzipien und Entwurfsmuster im Projekt

### Single Responsibility Principle

Ein zentrales Prinzip ist das **Single Responsibility Principle** aus SOLID. Die Klasse `Account` übernimmt ausschließlich die Verwaltung eines Bankkontos, einschließlich der Kontonummer, des Guthabens sowie der Geschäftslogik wie Einzahlungen, Auszahlungen und Überweisungen. Sie ist nicht mit zusätzlichen Verantwortlichkeiten wie Datenpersistenz oder Nutzerverwaltung überladen.

Ebenso ist die Klasse `AccountNumber` als sogenanntes **Value Object** klar auf die Verwaltung und Validierung der Kontonummer fokussiert. Diese Trennung sorgt für hohe Kohäsion und einfache Wartbarkeit.

---

### Encapsulation und GRASP-Prinzip des Information Expert

Darüber hinaus wird das Prinzip der Kapselung sowie das GRASP-Prinzip des Information Expert umgesetzt. Die `Account`-Klasse kapselt ihr Verhalten vollständig: Methoden wie `withdraw()`, `deposit()` und `transferTo()` kontrollieren die Abläufe innerhalb der Klasse und schützen so die Integrität des Kontostands und den Zustand des Kontos (z. B. Sperrstatus).

Nur die `Account`-Klasse kennt die internen Details, wie z. B. das Sperren des Kontos oder das Prüfen von ausreichendem Guthaben, was die Verantwortlichkeiten klar definiert und den Code **robust** macht.

---

### Dependency Inversion Principle (DIP)

Ein weiteres wichtiges Prinzip, das indirekt im Gesamtprojekt angewendet wird, ist das Dependency Inversion Principle aus SOLID. Insbesondere in der Service-Schicht kommuniziert die Logik über Interfaces wie `CustomerRepositoryPort` und `AccountRepositoryPort`. Dadurch wird die Kopplung an konkrete Implementierungen vermieden, was die Testbarkeit deutlich verbessert und die Flexibilität für zukünftige Anpassungen erhöht.

---

### Don’t Repeat Yourself (DRY)

Das Prinzip **Don’t Repeat Yourself (DRY)** wird konsequent beachtet. Die Verwendung von spezialisierten **Value Objects** wie `AccountNumber` und `Money` erlaubt es, wichtige Validierungen und Operationen (z. B. Addition und Subtraktion von Geldbeträgen) an einer zentralen Stelle zu kapseln. Das verhindert Code-Duplikate und erleichtert die Wartung, da Änderungen nur an einer Stelle notwendig sind.

---

### Open/Closed Principle (OCP)

Das **Open/Closed Principle (OCP)** wird berücksichtigt: Die `Account`-Klasse ist so gestaltet, dass sie für Erweiterungen offen, aber für Modifikationen geschlossen ist. Neue Funktionen, z. B. erweiterte Transaktionsarten oder zusätzliche Kontozustände, können hinzugefügt werden, indem man bestehende Klassen ergänzt oder erweitert, ohne vorhandenen Code zu verändern.

---

## Einsatz des Entwurfsmusters: Factory

Im Projekt wurde das Entwurfsmuster Factory (Erzeugermuster) gezielt eingesetzt und zwar in Form der Klasse `TransactionFactory`.

Dieses Muster dient dazu, die Erstellung komplexer Objekte von deren Nutzung zu entkoppeln und eine zentrale Stelle zur Objekterzeugung zu schaffen. Die `TransactionFactory` kapselt die Erzeugung verschiedener Arten von `Transactions`-Objekten (Einzahlungen, Auszahlungen, Überweisungen) und stellt dafür klar benannte Methoden bereit.

### Vorteile des Einsatzes:

- **Trennung von Erstellung und Logik**: Die Erstellung der `Transactions`-Objekte ist von der Geschäftslogik getrennt. Service- oder Domänenklassen müssen nicht selbst den Aufbau der Objekte kennen.
- **Erhöhung der Kohäsion**: Die Factory konzentriert sich auf die Objekterzeugung, was zu übersichtlicherem und verständlicherem Code führt.
- **Erweiterbarkeit**: Neue Transaktionstypen können an zentraler Stelle ergänzt werden, ohne bestehende Klassen zu verändern.
- **Konsistenz**: Attribute wie Zeitstempel, Transaktionstyp und beteiligte Konten werden einheitlich und fehlerfrei gesetzt.
- **Testbarkeit**: Die Erzeugung von Testobjekten wird vereinfacht und standardisiert.