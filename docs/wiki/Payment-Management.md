# Payment Management (5 scenarios)

Background
- Given the system is running
- And the client is authenticated as a guest

1. Process payment
- Given the guest has a pending reservation
- When the client calls POST /payments with a payment amount
- Then the client receives status code of 201
- And the payment status is set to processing

2. Confirm payment
- Given a payment attempt is successful
- When the client calls PUT /payments/1/confirm
- Then the client receives status code of 200
- And the reservation state changes to confirmed

3. Handle payment failure
- Given the client provides invalid payment details
- When the client calls POST /payments with a payment amount
- Then the client receives status code of 400
- And the reservation remains in a pending state

4. Confirm non-existing payment
- Given no payment exists
- When the client calls PUT /payments/999/confirm for non-existing payment
- Then the client receives status code of 404
- And an error message is returned

5. Confirm already confirmed payment
- Given a payment has already been confirmed
- When the client calls PUT /payments/1/confirm for already confirmed payment
- Then the client receives status code of 409
- And an error message is returned
