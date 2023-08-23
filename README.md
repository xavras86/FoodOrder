# FoodOrder!

To aplikacja Spring Boot + JPA + Spring MVC + Thymeleaf umożliwiająca użytkownikom zamawianie jedzenie z dowozem. Podstawowe funkjonalności realizowane w aplikacji to:

## Funkcjonalności właściciela:
- Właściciel lokalu gastronomicznego może założyć konto w portalu do zamawiania jedzenia, żeby móc oferować swoje usługi.
- Właściciel może mieć wiele różnych lokali
- Właściciel lokalu gastronomicznego może zdefiniować swoje menu, czyli określić kategorię jedzenia (przystawki, zupy, drugie dania, desery) i wprowadzić opis pozycji razem z ceną.
- Każda pozycja w menu możne zostać z niego wycofana lub przywrócona w zależności od dostępności produktu.
- Do każdej z podanych pozycji, właściciel lokalu gastronomicznego może wgrać do aplikacji zdjęcie, żeby ułatwić klientowi wybór.
- Właściciel lokalu gastronomicznego może podać listę ulic, na które dowozi jedzenie, ulice mogą być dodawane lub usuwane do zasięgu świadoczonych usług przez każdą restaurację.
- Właściciel lokalu gastronomicznego może zobaczyć złożone u niego zamówienia, z podziałem na oczekujące, zrealizowane i anulowane.
  -Właściciel lokalu może zmieniać status zamówień z aktywnych na zrealizowane.

## Funkcjonalności klienta:
- Klient może założyć konto.
- Klient może podać adres, żeby na tej podstawie aplikacja wyświetliła mu listę lokali oferujących możliwość zamówienia jedzenia z dowozem (na podstawie puli ulic ustalonej przez właściciela lokalu).
- Klient może wybrać lokal i zobaczyć jego szczegóły wraz menu oraz mapą Google Maps ze wskazanym adresem lokalu.
- Klient może złożyć zamówienie, zaznaczając ile i jakich pozycji chce zamówić.
- Klient może zobaczyć potwierdzenie złożonego zamówienia na podstawie wygenerowanego numeru zamówienia, wraz mapą Google Maps z zaznaczoną trasą między lokalem a adresem zamówienia.
- Klient może anulować zamówienie, jeżeli minęło mniej niż 20 minut od jego złożenia.
- Klient może sprawdzić swoje zamówienia z podziałem na zrealizowane, oczekujące i anulowane, wraz z odnośnikiem do karty ze szczegółami zamówienia.

## Funkcjonalności REST-API:
- Cześcią aplikacji jest REST-API, przeznaczone do wykorzystania biznesowo przez właściela (konta z przypisaną rolą właściciela mają dostęp do API).
- Składają sie na nie dwa kontrolery umożliwiające wywołanie endpointów GET, POST, PUT i DELETE związane z restauracjami i zamówieniami.
- Szczegóły funkcjonalności API zostały opisane w ramach dokumentacji Swagger UI.

## Instalacja

1. Sklonuj to repozytorium na swój lokalny komputer.
2. Aplikacja jest przygotowana do uruchomienia w kontenerze przez Docker Compose, po uruchomieniu aplikacji i dockera uruchom kontener:

```
./gradlew clean build -x test
docker compose up -d
```

3. Aplikacja jest dostępna z poziomu przeglądarki pod adresem:
```
http://localhost:8190/foodorder/login
```

4. API wraz z dokumentacją SwaggerUI jest dostępne z poziomu przeglądarki pod adresem (konieczne zalogowanie poświadczeniami z rolą właściciel)
```
http://localhost:8190/foodorder/swagger-ui/index.html
```
5. Przy uruchomieniu aplikacja jest napełniana danymi rozruchowymi. Tworzone są również przykładowe konta dla właściciela i klienta
```
Właściciel - login: owner password: test
```

```
Kliet - login: customer password: test
```

## Wykorzystane technologie

1.Aplikacja została przygotowana z wykorzystaniem Spring Boot i bazy danych PostgreSQL. Schemat przedstawia tabele bazy wraz z konfiguracją relacji.

![](https://github.com/xavras86/FoodOrder/assets/99759304/f12ac45d-05ce-48f2-ba95-0a57d2fa485a)

2. Struktura samej aplikacji po stronie Java opiera się o model warstwowy z wyszczególnioną warstwą repozytoriów, serwisó i kontrollerów wraz z odpowiadającymi im warstwami obiektów
   tj. encji, obiektów domenowych i DTO, mapowania między poszczególnymi warstwami zostały zrealizowane z wykorzystaniem Mapstruct.

![](https://github.com/xavras86/FoodOrder/assets/99759304/2b93f9fb-186c-4301-8ec0-62341c4deaaa)

3. Dane rozruchowe są ładowane do aplikacji przy uruchopmieniu z wykorzystaniem skryptów migracyjnych Flyway.

4. Warstwa WEB została przygotowana z wykorzystaniem silnika szablonów Thymeleaf.


## Autor
Marcin Sikora
