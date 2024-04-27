CREATE TABLE Library
(
    library_id       INTEGER PRIMARY KEY AUTOINCREMENT,
    name             TEXT NOT NULL,
    location         TEXT,
    opening_hours    TEXT,
    established_date DATE,
    website          TEXT,
    email            TEXT,
    phone_number     TEXT,
    is_open          BOOLEAN
);

CREATE TABLE Address
(
    address_id  INTEGER PRIMARY KEY AUTOINCREMENT,
    library_id  INTEGER,
    street      TEXT,
    city        TEXT,
    state       TEXT,
    country     TEXT,
    postal_code TEXT,
    FOREIGN KEY (library_id) REFERENCES Library (library_id)
);

CREATE TABLE Author
(
    author_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    name        TEXT NOT NULL,
    bio         TEXT,
    nationality TEXT,
    birth_date  DATE,
    death_date  DATE,
    website     TEXT
);

CREATE TABLE Book
(
    book_id      INTEGER PRIMARY KEY AUTOINCREMENT,
    title        TEXT NOT NULL,
    isbn         TEXT,
    publish_year INTEGER,
    edition      TEXT,
    language     TEXT,
    genre        TEXT,
    library_id   INTEGER,
    author_id    INTEGER,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (library_id) REFERENCES Library (library_id),
    FOREIGN KEY (author_id) REFERENCES Author (author_id)
);

CREATE TABLE Tag
(
    tag_id      INTEGER PRIMARY KEY AUTOINCREMENT,
    name        TEXT NOT NULL,
    description TEXT
);

CREATE TABLE Book_Tag
(
    book_id INTEGER,
    tag_id  INTEGER,
    PRIMARY KEY (book_id, tag_id),
    FOREIGN KEY (book_id) REFERENCES Book (book_id),
    FOREIGN KEY (tag_id) REFERENCES Tag (tag_id)
);

CREATE TABLE User
(
    user_id       INTEGER PRIMARY KEY AUTOINCREMENT,
    username      TEXT NOT NULL,
    email         TEXT NOT NULL,
    password      TEXT NOT NULL,
    full_name     TEXT,
    date_of_birth DATE,
    gender        TEXT
);

CREATE TABLE Borrowed_Book
(
    borrowed_id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id     INTEGER,
    user_id     INTEGER,
    borrow_date DATE,
    return_date DATE,
    FOREIGN KEY (book_id) REFERENCES Book (book_id),
    FOREIGN KEY (user_id) REFERENCES User (user_id)
);

CREATE TABLE Review
(
    review_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id     INTEGER,
    user_id     INTEGER,
    rating      DECIMAL(3, 2),
    comment     TEXT,
    review_date DATE,
    FOREIGN KEY (book_id) REFERENCES Book (book_id),
    FOREIGN KEY (user_id) REFERENCES User (user_id)
);

CREATE TABLE Publisher
(
    publisher_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name         TEXT NOT NULL,
    website      TEXT
);

CREATE TABLE Book_Publisher
(
    book_id      INTEGER,
    publisher_id INTEGER,
    PRIMARY KEY (book_id, publisher_id),
    FOREIGN KEY (book_id) REFERENCES Book (book_id),
    FOREIGN KEY (publisher_id) REFERENCES Publisher (publisher_id)
);

CREATE TABLE Library_Staff
(
    staff_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    library_id INTEGER,
    user_id    INTEGER,
    role       TEXT,
    FOREIGN KEY (library_id) REFERENCES Library (library_id),
    FOREIGN KEY (user_id) REFERENCES User (user_id)
);

CREATE TABLE Membership
(
    membership_id     INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id           INTEGER,
    library_id        INTEGER,
    join_date         DATE,
    expiration_date   DATE,
    membership_status TEXT,
    FOREIGN KEY (user_id) REFERENCES User (user_id),
    FOREIGN KEY (library_id) REFERENCES Library (library_id)
);

CREATE TABLE Library_Event
(
    event_id    INTEGER PRIMARY KEY AUTOINCREMENT,
    library_id  INTEGER,
    event_name  TEXT,
    event_date  DATE,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (library_id) REFERENCES Library (library_id)
);

CREATE TABLE Event_Participant
(
    event_id INTEGER,
    user_id  INTEGER,
    PRIMARY KEY (event_id, user_id),
    FOREIGN KEY (event_id) REFERENCES Library_Event (event_id),
    FOREIGN KEY (user_id) REFERENCES User (user_id)
);

CREATE TRIGGER update_book_created_at
    AFTER INSERT
    ON Book
    FOR EACH ROW
BEGIN
    UPDATE Book SET created_at = CURRENT_TIMESTAMP WHERE book_id = NEW.book_id;
END;

CREATE TRIGGER update_book_updated_at
    AFTER UPDATE
    ON Book
    FOR EACH ROW
BEGIN
    UPDATE Book SET updated_at = CURRENT_TIMESTAMP WHERE book_id = NEW.book_id;
END;
