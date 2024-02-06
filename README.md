Technology stack

Groovy
Spring Boot framework
MongoDB database
Gradle builder
Docker

App:

API for SNS. The user can register, log in, log out, leave a post, see his feed (a list of posts by people he follows), see another user's feed (without his subscriptions), comment on a post, leave or remove a like on a post (his own or someone else's), and follow another user.

Queries

Create a user
Log in / Log out
Edit a user
Delete a user
Create a post
Editing a post
Deleting a post
Leave/delete a post favorite
Subscription to a user (the subscriber's feed will receive posts from the user to whom he/she subscribed)
Unsubscribe from a user
Commenting on a post
Get a user's feed (including likes and comments)
Get another user's feed
Get post comments
Leave or remove a like on a post (his own or someone else's)
Likes can be left and deleted.

All queries can be accessed and rewieved on:
http://localhost:8080/swagger-ui/index.html
after project start up
