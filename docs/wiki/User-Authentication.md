# User Authentication (4 scenarios)

Background
- Given the system is running

1. Successful user registration
- Given the client provides valid registration details
- When the client calls POST /users/register
- Then the auth client receives status code of 201
- And a new user account is created

2. Registration with already registered email
- Given the client provides registration details with an existing email
- When the client calls POST /users/register
- Then the auth client receives status code of 409
- And an authentication error message is returned

3. Successful login
- Given the client provides valid login credentials
- When the client calls POST /users/login
- Then the auth client receives status code of 200
- And the system returns an authentication token

4. Successful logout
- Given the client is logged in
- When the client calls POST /users/logout
- Then the auth client receives status code of 200
- And the authentication token is invalidated
