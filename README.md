# Crime Map Application
This is a full-stack web application designed to allow users to report and view local incidents on an interactive map in real-time.

**Disclaimer:** This project was primarily built as a learning exercise. The main goal was to learn the basics of **Spring Security** (JWT, CORS, CSRF, stateless sessions) and backend architecture using Spring Boot. The accent is heavily on the backend infrastructure. The frontend was built by an Angular beginner to serve as a functional client to test the API, so the UI code might not follow enterprise best practices.

## Tech Stack
* **Backend:** Java 25, Spring Boot, Spring Security, Spring Data JPA.
* **Frontend:** Angular, TypeScript, Leaflet.js, `@microsoft/fetch-event-source`.
* **Database:** PostgreSQL.
* **Features:** JWT Authentication, Server-Sent Events for real-time updates.

## How to Run (Docker)

The entire application is containerized and can be run using Docker Compose.

### Prerequisites
* Docker Desktop installed and running.
* Ports `80`, `8080`, and `5432` must be free on your machine.

### Setup Instructions
1. Clone the repository and navigate to the root directory.
2. Create a `.env` file in the root directory. An example file:
   ```
   POSTGRES_USER=postgres
   POSTGRES_PASSWORD=password
   SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/crime
   JWT_SECRET=JWTSecretWhichShouldBeAtLeast64CharactersLongAndKeptSecret123456
   ```
3. Build and run:
   `docker-compose up -d --build`
4. Access the App: Open your browser and go to `http://localhost`.

## Known Flaws & Limitations
1. The SSE Bug: The live-updating map uses Server-Sent Events. If you switch to another tab, the SSE connection will pause. If another user drops a pin during this time, your client will miss it.
2. Angular Architecture: The frontend was built to "just make it work" so I could test the backend. The code is not optimized.
3. Timezone Handling: The application currently relies on a Docker environment variable (TZ=Europe/Warsaw) to synchronize the backend clock with the local time to prevent the "incident in the future" validation errors.

## Roadmap & Future Improvements
* Implement "Sync-on-Wake": Update the Angular frontend to fetch all pins via a standard GET request immediately upon reconnecting to the SSE stream to catch any missed events.
* UTC Everywhere: Refactor the backend to strictly use UTC time for all database records, and force the Angular frontend to convert local times to ISO strings before sending POST requests.
* Role-Based Access: Add ADMIN and USER roles in Spring Security to allow admins to delete false incident reports.
* Severity Score: Make incidents with high severity score stay on map longer.
* Heatmap: Add an ability for the user to toggle the heatmap of recent incidents.
* User Trust Score: Users with low trust score will be prohibited from adding new pins to the map.
