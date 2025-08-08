# Wemos D1 mini + BME280 firmware

Ten folder zawiera kod na mikrokontroler Wemos D1 mini.

## Funkcje
- Odczyt temperatury, wilgotności, ciśnienia z BME280
- Wysyłka danych POST do backendu co 5 minut
- Miganie diodą co 10 sekund w przypadku poprawnego połączenia Wi-Fi

## Konfiguracja
1. Otwórz plik `wemos-bme280.ino` w Arduino IDE.
2. Wpisz swoje dane Wi-Fi i adres backendu w sekcji KONFIG.
3. Wgraj kod na Wemosa (płytka: LOLIN(WEMOS) D1 mini).
