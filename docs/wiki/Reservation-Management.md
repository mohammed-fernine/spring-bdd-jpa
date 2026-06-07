# Reservation Management (25 scenarios)

Background
- Given the system is running
- And I have appropriate user privileges

1. Create a new reservation
- Given I want to create a new reservation
- And a room with ID 1 is available
- And a guest with ID 1 exists
- And a hotel with ID 1 exists
- When I send a POST request to "/api/reservations" with valid reservation data
- Then the response status should be 201 CREATED
- And the response should contain reservation details
- And the reservation should be saved in the database
- And the reservation should have PENDING status

2. Attempt to create reservation for unavailable room
- Given a room with ID 1 is unavailable
- And a guest with ID 1 exists
- When I send a POST request to "/api/reservations" with roomId 1
- Then the response status should be 409 CONFLICT
- And the response should contain an error message

3. Attempt to create reservation with conflicting dates
- Given a room with ID 1 has a reservation for a date range
- And a guest with ID 1 exists
- When I send a POST request to "/api/reservations" with overlapping dates
- Then the response status should be 409 CONFLICT
- And the response should contain an error message

4. Get all reservations
- Given there are reservations in the system
- When I send a GET request to "/api/reservations"
- Then the response status should be 200 OK
- And the response should contain a list of reservations
- And each reservation should have id, room, guest, hotel, and status

5. Get reservation by ID
- Given a reservation with ID 1 exists
- When I send a GET request to "/api/reservations/1"
- Then the response status should be 200 OK
- And the response should contain reservation details
- And the reservation should have the correct ID

6. Get reservations by guest
- Given a guest with ID 1 has reservations
- When I send a GET request to "/api/reservations/guest/1"
- Then the response status should be 200 OK
- And the response should contain reservations for guest 1
- And all reservations should belong to guest 1

7. Get reservations by hotel
- Given a hotel with ID 1 has reservations
- When I send a GET request to "/api/reservations/hotel/1"
- Then the response status should be 200 OK
- And the response should contain reservations for hotel 1
- And all reservations should belong to hotel 1

8. Get reservations by room
- Given a room with ID 1 has reservations
- When I send a GET request to "/api/reservations/room/1"
- Then the response status should be 200 OK
- And the response should contain reservations for room 1
- And all reservations should belong to room 1

9. Get reservations by status
- Given there are reservations with different statuses
- When I send a GET request to "/api/reservations/status/PENDING"
- Then the response status should be 200 OK
- And the response should contain only pending reservations
- And all reservations should have PENDING status

10. Get reservations by guest and status
- Given a guest with ID 1 has reservations
- When I send a GET request to "/api/reservations/guest/1/status/CONFIRMED"
- Then the response status should be 200 OK
- And the response should contain confirmed reservations for guest 1
- And all reservations should belong to guest 1 and have CONFIRMED status

11. Update existing reservation
- Given a reservation with ID 1 exists
- And the reservation has PENDING status
- When I send a PUT request to "/api/reservations/1" with valid reservation data
- Then the response status should be 200 OK
- And the response should contain updated reservation details
- And the reservation should be updated in the database

12. Confirm a pending reservation
- Given a reservation with ID 1 has PENDING status
- When I send a PUT request to "/api/reservations/1/confirm"
- Then the response status should be 200 OK
- And the reservation should have CONFIRMED status
- And the reservation should be updated in the database

13. Cancel a pending reservation
- Given a reservation with ID 1 has PENDING status
- When I send a PUT request to "/api/reservations/1/cancel"
- Then the response status should be 200 OK
- And the reservation should have CANCELED status
- And the reservation should be updated in the database

14. Cancel a confirmed reservation
- Given a reservation with ID 1 has CONFIRMED status
- When I send a PUT request to "/api/reservations/1/cancel"
- Then the response status should be 200 OK
- And the reservation should have CANCELED status
- And the reservation should be updated in the database

15. Complete a confirmed reservation
- Given a reservation with ID 1 has CONFIRMED status
- When I send a PUT request to "/api/reservations/1/complete"
- Then the response status should be 200 OK
- And the reservation should have COMPLETED status
- And the reservation should be updated in the database

16. Attempt to complete a pending reservation
- Given a reservation with ID 1 has PENDING status
- When I send a PUT request to "/api/reservations/1/complete"
- Then the response status should be 400 BAD REQUEST
- And the response should contain an error message

17. Attempt to cancel a completed reservation
- Given a reservation with ID 1 has COMPLETED status
- When I send a PUT request to "/api/reservations/1/cancel"
- Then the response status should be 400 BAD REQUEST
- And the response should contain an error message

18. Delete existing reservation
- Given a reservation with ID 1 exists
- When I send a DELETE request to "/api/reservations/1"
- Then the response status should be 204 NO CONTENT
- And the reservation should be removed from the database

19. Attempt to delete non-existent reservation
- Given no reservation with ID 999 exists
- When I send a DELETE request to "/api/reservations/999"
- Then the response status should be 404 NOT FOUND
- And the response should contain an error message

20. Get non-existent reservation by ID
- Given no reservation with ID 999 exists
- When I send a GET request to "/api/reservations/999"
- Then the response status should be 404 NOT FOUND
- And the response should contain an error message

21. Validate reservation creation with invalid data
- When I send a POST request to "/api/reservations" with invalid data (e.g., numberOfGuests 0)
- Then the response status should be 400 BAD REQUEST
- And the response should contain validation errors

22. Validate reservation update with invalid data
- Given a reservation with ID 1 exists
- When I send a PUT request to "/api/reservations/1" with invalid data (e.g., numberOfGuests 0)
- Then the response status should be 400 BAD REQUEST
- And the response should contain validation errors

23. Create reservation with non-existent room
- Given no room with ID 999 exists
- And a guest with ID 1 exists
- When I send a POST request to "/api/reservations" with roomId 999
- Then the response status should be 400 BAD REQUEST
- And the response should contain an error message

24. Create reservation with non-existent guest
- Given a room with ID 1 exists
- And no guest with ID 999 exists
- When I send a POST request to "/api/reservations" with guestId 999
- Then the response status should be 400 BAD REQUEST
- And the response should contain an error message

25. Create reservation with invalid date range
- Given a room with ID 1 exists
- And a guest with ID 1 exists
- When I send a POST request to "/api/reservations" with invalid date range
- Then the response status should be 400 BAD REQUEST
- And the response should contain an error message
