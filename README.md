# Environmental Monitoring Station (IoT)

A compact IoT project for monitoring environmental conditions (temperature, humidity, pressure) using an ESP8266 and a Spring Boot backend. Sensor data is stored in MySQL and visualized on a real-time web dashboard.

---

## Architecture

BME280 → ESP8266 (Wemos D1 Mini) → Spring Boot REST API → MySQL → Web Dashboard

---

## Tech Stack

- Hardware: ESP8266 (Wemos D1 Mini), BME280
- Backend: Java 17, Spring Boot, Spring Data JPA, MySQL
- Frontend: JSP, Chart.js (zoom/pan)
- Other: JAXB (XML export)

---

## Features

- HTTP JSON ingestion from ESP8266
- Persistent storage in MySQL
- Dashboard with:
  - current temperature, humidity, pressure
  - 6-hour line chart
  - auto refresh (10s)
  - zoom & pan
- UTC timestamps with frontend timezone conversion
- XML data export

---

## Run

Start the backend with: mvn spring-boot:run  
Dashboard: http://localhost:8080

---

## ESP8266

Sends measurements every 5 minutes as JSON: { "temperature": 24.6, "humidity": 55.2, "pressure": 1013.4 }  
Endpoint: POST /api/readings

---

## Export

GET /api/readings/export/xml

---

## Author

Engineering portfolio project focused on IoT and Java backend development.

