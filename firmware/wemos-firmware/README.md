# Wemos D1 mini + BME280 Firmware

This folder contains the firmware for the Wemos D1 mini microcontroller.

---

## Features

- Reading temperature, humidity, and pressure from the BME280 sensor
- Sending sensor data to the backend via HTTP POST every 5 minutes
- LED blink every 10 seconds when Wi-Fi connection is established

---

## Configuration

1. Open `wemos-bme280.ino` in Arduino IDE
2. Fill in your Wi-Fi credentials and backend URL in the `KONFIG` section
3. Upload the code to the board:
   - Board: **LOLIN(WEMOS) D1 mini**

