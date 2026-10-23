# HotelEase – Hotel Booking and Reservation System

**Group 4 | Section C2B**
Maligaya, Catherine • Maandal, Gimuel • Vergara, Zuichi • Zulueta, Carlo

HotelEase is a console-based Java system that helps a small hotel manage rooms, guests and reservations.
It has a login with two roles (**Admin** and **Receptionist**), CRUD operations, input validation,
exception handling, and two GoF design patterns (**Singleton** and **Strategy**).

## How to Run

Requires JDK 11 or higher.

```bash
cd src
javac *.java
java Main
```

(Or open the `src` folder in any IDE such as IntelliJ / NetBeans / Eclipse and run `Main.java`.)

### Default Accounts

| Role         | Username     | Password   |
|--------------|--------------|------------|
| Admin        | `admin`      | `admin123` |
| Receptionist | `reception1` | `recep123` |

## Features

| Role | What they can do |
|------|------------------|
| **Admin** | Add / view / search / update / delete rooms • set a room to Maintenance • add / view / update / delete Receptionist accounts • view guests and bookings |
| **Receptionist** | Add / view / search / update / delete guests • create / view / search / update / confirm / cancel bookings • check room availability • check guests in and out |

The Receptionist **cannot** manage rooms or user accounts.

### Business Rules
- A room cannot be double-booked: overlapping dates are rejected (check-out day is free for the next guest).
- Rooms under Maintenance cannot be booked.
- Room status (Available / Reserved / Occupied / Maintenance) updates automatically as bookings change.
- Booking status flow: `Pending → Confirmed → Checked-in → Checked-out` (or `Cancelled` from Pending/Confirmed).
- A guest or room that has booking records cannot be deleted (booking history is kept). A room with active bookings cannot be set to Maintenance.
- A guest can only be checked in on or after the booking's check-in date.
- Stay dates: not in the past, not more than 1 year ahead, check-out after check-in, maximum 30 nights.

### Validations
Blank fields • duplicate IDs / usernames / room numbers • number format and range • date format and logic •
name format (accented letters allowed) • phone number (`09XXXXXXXXX`) • email format • ID formats (`G001`, `B001`) • username and password rules •
menu choices out of range • non-existent records • invalid status changes • unavailable rooms • login attempt limit (3).

## Requirements Checklist

| Requirement | Where it is used |
|-------------|------------------|
| **Encapsulation** | Private fields with getters/setters in `Room`, `Guest`, `Booking`, `User` |
| **Abstraction** | `User` (abstract class), `PricingStrategy` (interface) |
| **Inheritance** | `Admin` and `Receptionist` extend `User`; custom exceptions extend `HotelException` |
| **Polymorphism** | `showMenu()` / `getRole()` overridden in each user type; `calculatePrice()` implemented by each pricing class |
| **Exception Handling** | try-catch with custom exceptions (`InvalidInputException`, `DuplicateRecordException`, `RecordNotFoundException`, `RoomUnavailableException`) and `NumberFormatException` / `DateTimeParseException` handling |
| **Singleton** | `HotelSystem` – private constructor + `getInstance()`; one shared object holds all data |
| **Strategy** | `PricingStrategy` with `StandardPricing`, `PeakSeasonPricing`, `LongStayPricing` |
| **Login + Roles** | `Main.login()` and `HotelSystem.login()` → opens Admin or Receptionist menu |
| **CRUD** | Rooms, Guests, Bookings, Receptionist accounts |

## Class Overview

```
src/
├── Main.java                      Entry point and login
├── HotelSystem.java               Singleton: stores data + business rules
├── User.java (abstract)           Parent of Admin and Receptionist
├── Admin.java / Receptionist.java Role menus
├── Room.java / Guest.java / Booking.java   Data classes
├── RoomStatus.java / BookingStatus.java    Enums
├── PricingStrategy.java (interface)
├── StandardPricing / PeakSeasonPricing / LongStayPricing
├── InputHelper.java               Input reading and validation
├── Display.java                   Table printing
└── HotelException + 4 child exceptions
```

## Notes
- Data is stored in memory only (it resets when the program closes), with a few sample rooms and guests loaded at startup.
