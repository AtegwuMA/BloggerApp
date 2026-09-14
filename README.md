# BloggerApp

A blogging platform API. Users register, create posts with images, comment and reply on posts, like content, and search across posts.

## Stack

Java, Spring Boot, Spring Data JPA, Postgres.

## Features

- User registration and login
- Create, update, and delete posts, with image uploads
- Comment on posts, with threaded replies
- Like posts and comments
- Search across posts
- Category tagging on posts (design categories)

## Running it

You'll need a local Postgres database. Update the connection details in `src/main/resources/application.properties`, then:

```bash
./mvnw spring-boot:run
```

The app starts on `http://localhost:9078`.

## API overview

All endpoints are under `/api`.

**Users**
- `POST /users/register`
- `POST /users/login`

**Posts** (under `/users/{userId}`)
- `GET /posts` – list posts
- `GET /posts/{postId}` – get a single post
- `POST /posts/{postId}` – create/update a post
- `DELETE /posts/{postId}` – delete a post
- `POST /designs` – tag a post with a design category

**Comments**
- `GET /comments/posts/{postId}/comments` – list comments on a post
- `POST /comments/posts/{postId}/comments` – add a comment
- `GET /comments/posts/{postId}/comments/{commentId}` – get a comment
- `PUT /comments/posts/{postId}/comments/{commentId}` – edit a comment
- `DELETE /comments/posts/{postId}/comments/{commentId}` – delete a comment

**Search**
- `GET /search/search` – search posts

## Note on secrets

The Postgres password in `src/main/resources/application.properties` is a real, committed credential from local development. Rotate that password and remove it from the file (use an environment variable instead) before treating this repo as public-facing.
