CREATE TABLE IF NOT EXISTS users (
    id INTEGER IDENTITY PRIMARY KEY,
    name varchar(50) NOT NULL,
    surname varchar(50) NOT NULL,
    mail varchar(50) UNIQUE NOT NULL,
    password char(60) NOT NULL,
    profile_img_url VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS search_zones (
    zoneId INTEGER IDENTITY PRIMARY KEY,
    latitude double precision NOT NULL,
    longitude double precision NOT NULL,
    range INTEGER CHECK(range > 0),
    userId INTEGER REFERENCES users(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS posts (
    postId INTEGER IDENTITY PRIMARY KEY,
    title    VARCHAR(64) NOT NULL,
    description VARCHAR(2048) NOT NULL,
    contact_phone    VARCHAR(32) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    category VARCHAR(32) NOT NULL CHECK (category IN ('LOST', 'FOUND', 'ADOPT', 'EMERGENCY')),
    pet VARCHAR(32) NOT NULL CHECK (pet IN ('DOG', 'CAT', 'OTHER')),
    is_male BOOLEAN NOT NULL,
    latitude double precision NOT NULL,
    longitude double precision NOT NULL,
    userId INTEGER REFERENCES users(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS postImages (
    postImageId INTEGER IDENTITY PRIMARY KEY,
    postId INTEGER REFERENCES posts(postId) ON DELETE CASCADE NOT NULL,
    url VARCHAR(32) NOT NULL,
    UNIQUE(postId, url)
);

CREATE TABLE IF NOT EXISTS comments (
    commentId INTEGER IDENTITY PRIMARY KEY NOT NULL,
    commentContent VARCHAR(1024) NOT NULL,
    commentDate TIMESTAMP NOT NULL,
    userId INTEGER REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    postId INTEGER REFERENCES posts(postId) ON DELETE CASCADE NOT NULL,
    parentId INTEGER REFERENCES comments(commentId) ON DELETE CASCADE,
    UNIQUE(commentDate, userId, postId)
);