# Booking Management (5 scenarios)

Background
- Given the system is running
- And the client is authenticated as a guest

1. Create booking
- Given the client is authenticated as a guest
- When the client calls POST /api/reservations with reservation details
- Then the client receives status code of 201
- And the booking status is set to pending

2. Cancel booking
- Given the guest has a confirmed reservation with ID 1
- When the client calls PUT /api/reservations/1/cancel
- Then the client receives status code of 200
- And the reservation status is marked as canceled

3. View booking details
- Given the guest has an existing reservation with ID 1
- When the client calls GET /api/reservations/1 for reservation details
- Then the client receives status code of 200
- And the system returns the reservation details

4. List user bookings
- Given the client is authenticated as a guest
- When the client calls GET /api/reservations/guest/1 for user bookings
- Then the client receives status code of 200
- And the system returns a list of the user's reservations

5. Check room availability
- Given the user provides check-in and check-out dates
- When the client calls GET /api/rooms for room availability
- Then the client receives status code of 200
- And the system returns a list of available rooms
