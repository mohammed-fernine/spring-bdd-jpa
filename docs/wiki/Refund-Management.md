# Refund Management (6 scenarios)

Background
- Given the system is running
- And the client is authenticated as a guest

1. Request refund
- Given a guest has a canceled reservation eligible for a refund
- When the client calls POST /refunds with the reservation ID
- Then the client receives status code of 200
- And the refund request is recorded as pending

2. Approve refund
- Given a pending refund request exists
- When the administrator calls PUT /refunds/1/approve
- Then the administrator receives status code of 200
- And the refund status is updated to approved

3. Process refund
- Given an administrator has approved a refund request
- When the system processes the transaction via PUT /refunds/1/process
- Then the client receives status code of 200
- And the refund status is marked as completed

4. Reject refund request for invalid reservation
- Given a reservation is not eligible for a refund
- When the client calls POST /refunds with an invalid reservation ID
- Then the client receives status code of 200
- And an error message is returned for refunds

5. Approve non-existing refund request
- Given no pending refund request exists
- When the administrator calls PUT /api/payments/999/refund for non-existing refund
- Then the client receives status code of 200
- And an error message is returned for refunds

6. Process refund without approval
- Given a refund request exists but is not approved
- When the system processes the transaction via PUT /api/payments/1/refund without approval
- Then the client receives status code of 200
- And an error message is returned for refunds
