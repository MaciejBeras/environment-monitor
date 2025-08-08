#include <Wire.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>

// ====== CONFIG ======
const char* WIFI_SSID     = "WiFi name";      // Your WiFi SSID
const char* WIFI_PASS     = "Wifi password";  // Your WiFi password
const char* SERVER_URL    = "http://user_IP:8080/api/readings"; // Backend API endpoint

const char* DEVICE_ID     = "station001"; // Unique ID of the monitoring station
const uint8_t BME_ADDR    = 0x76;         // I2C address of the BME280 sensor

// Intervals (milliseconds)
//const unsigned long SEND_INTERVAL  = 10UL * 1000UL; // every 10 seconds
const unsigned long SEND_INTERVAL  = 5UL * 60UL * 1000UL; // every 5 minutes
const unsigned long BLINK_INTERVAL = 10UL * 1000UL; // blink every 10 seconds
const unsigned long BLINK_DURATION = 150;           // how long LED stays ON (ms)
// =====================

Adafruit_BME280 bme;

unsigned long lastSendMs  = 0;
unsigned long lastBlinkMs = 0;
bool          blinkActive = false;
unsigned long blinkStart  = 0;

void setup() {
  Serial.begin(115200);
  delay(300);

  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, HIGH); // HIGH = LED OFF (ESP8266 LED is active LOW)

  // Initialize BME280
  if (!bme.begin(BME_ADDR)) {
    Serial.println(F("[BME280] Sensor not found (try address 0x77)."));
    while (true) {
      blinkError(2); // 2 quick blinks in a loop = sensor error
    }
  }

  // Connect to WiFi
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASS);
  Serial.print(F("[WiFi] Connecting"));
  unsigned long start = millis();
  while (WiFi.status() != WL_CONNECTED && millis() - start < 15000) {
    Serial.print(".");
    delay(300);
  }
  Serial.println();

  if (WiFi.status() == WL_CONNECTED) {
    Serial.print(F("[WiFi] Connected, IP: "));
    Serial.println(WiFi.localIP());
    // Triple blink to confirm successful connection
    for (int i = 0; i < 3; i++) {
      digitalWrite(LED_BUILTIN, LOW);  delay(120);
      digitalWrite(LED_BUILTIN, HIGH); delay(120);
    }
  } else {
    Serial.println(F("[WiFi] Failed to connect at startup, retrying in background..."));
  }
}

void loop() {
  ensureWiFi();

  unsigned long now = millis();

  // --- SEND DATA EVERY X MINUTES ---
  if (now - lastSendMs >= SEND_INTERVAL) {
    lastSendMs = now;
    if (WiFi.status() == WL_CONNECTED) {
      sendReading();
    } else {
      Serial.println(F("[HTTP] Skipping send: no WiFi"));
    }
  }

  // --- BLINK LED EVERY 10 SECONDS (only if WiFi is connected) ---
  if (WiFi.status() == WL_CONNECTED) {
    if (!blinkActive && (now - lastBlinkMs >= BLINK_INTERVAL)) {
      lastBlinkMs = now;
      blinkActive = true;
      blinkStart  = now;
      digitalWrite(LED_BUILTIN, LOW); // LED ON (active LOW)
    }
    if (blinkActive && (now - blinkStart >= BLINK_DURATION)) {
      blinkActive = false;
      digitalWrite(LED_BUILTIN, HIGH); // LED OFF
    }
  } else {
    // If no WiFi, make sure LED is OFF
    if (blinkActive) {
      blinkActive = false;
      digitalWrite(LED_BUILTIN, HIGH);
    }
  }

  delay(2); // Short yield for WiFi stack
}

// ====== HELPER FUNCTIONS ======

void ensureWiFi() {
  if (WiFi.status() == WL_CONNECTED) return;

  static unsigned long lastTry = 0;
  unsigned long now = millis();
  if (now - lastTry < 5000) return; // retry every 5 seconds
  lastTry = now;

  Serial.print(F("[WiFi] Reconnecting... "));
  WiFi.disconnect();
  WiFi.begin(WIFI_SSID, WIFI_PASS);

  unsigned long t0 = millis();
  while (WiFi.status() != WL_CONNECTED && millis() - t0 < 5000) {
    Serial.print(".");
    delay(250);
  }
  Serial.println();

  if (WiFi.status() == WL_CONNECTED) {
    Serial.print(F("[WiFi] Connected, IP: "));
    Serial.println(WiFi.localIP());
    // Short blink to confirm
    digitalWrite(LED_BUILTIN, LOW);  delay(120);
    digitalWrite(LED_BUILTIN, HIGH); delay(120);
  } else {
    Serial.println(F("[WiFi] Still offline"));
  }
}

void sendReading() {
  float t = bme.readTemperature();
  float h = bme.readHumidity();
  float p = bme.readPressure() / 100.0F; // Pa -> hPa

  if (isnan(t) || isnan(h) || isnan(p)) {
    Serial.println(F("[BME280] Invalid readings, skipping POST"));
    return;
  }

  // Build JSON manually
  String payload = "{";
  payload += "\"deviceId\":\"" + String(DEVICE_ID) + "\",";
  payload += "\"temperature\":" + String(t, 2) + ",";
  payload += "\"humidity\":"    + String(h, 2) + ",";
  payload += "\"pressure\":"    + String(p, 2);
  payload += "}";

  WiFiClient client;           // HTTP over TCP
  HTTPClient http;
  http.setReuse(false);        // avoid keep-alive issues
  http.setTimeout(4000);       // 4s timeout

  if (http.begin(client, SERVER_URL)) {
    http.addHeader("Content-Type", "application/json");
    int code = http.POST(payload);

    if (code > 0) {
      Serial.printf("[HTTP] Response: %d\n", code);
    } else {
      Serial.printf("[HTTP] POST error: %s\n", http.errorToString(code).c_str());
    }
    http.end();
  } else {
    Serial.println(F("[HTTP] Failed to initialize connection"));
  }
}

void blinkError(uint8_t times) {
  for (uint8_t i = 0; i < times; i++) {
    digitalWrite(LED_BUILTIN, LOW);  delay(120);
    digitalWrite(LED_BUILTIN, HIGH); delay(120);
  }
  delay(800);
}
