# Ratings (6 scenarios)

Background
- Given the system is running
- And the client is authenticated as a guest

1. Add a rating
- Given a guest has completed their stay
- When the client calls POST /hotels/1/ratings with a score
- Then the client receives status code of 201
- And the rating is saved

2. View average rating
- Given a hotel has ratings
- When the client calls GET /hotels/1/ratings/average
- Then the client receives status code of 200
- And the system returns the average rating

3. Update a rating
- Given a guest has submitted a rating
- When the client calls PUT /ratings/1 with a new score
- Then the client receives status code of 200
- And the rating is updated

4. Reject invalid rating
- Given a guest has completed their stay
- When the client calls POST /hotels/1/ratings with an invalid score
- Then the client receives status code of 400
- And an error message is returned for ratings

5. Update non-existing rating
- Given no rating exists with ID 999
- When the client calls PUT /ratings/999 with a score
- Then the client receives status code of 404
- And an error message is returned for ratings

6. View average rating for hotel without ratings
- Given a hotel has no ratings
- When the client calls GET /hotels/1/ratings/average
- Then the client receives status code of 200
- And the system returns 0 as the average rating
