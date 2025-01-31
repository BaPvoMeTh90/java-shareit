DROP TABLE IF EXISTS COMMENTS;
DROP TABLE IF EXISTS BOOKINGS;
DROP TABLE IF EXISTS ITEMS;
DROP TABLE IF EXISTS REQUESTS;
DROP TABLE IF EXISTS USERS;

CREATE TABLE IF NOT EXISTS USERS (
    ID    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    EMAIL VARCHAR(255) NOT NULL UNIQUE,
    NAME  VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS REQUESTS (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    DESCRIPTION VARCHAR(1000) NOT NULL,
    REQUESTOR_ID BIGINT NOT NULL,
    CREATED TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_request_to_user FOREIGN KEY (REQUESTOR_ID) REFERENCES USERS (ID)
);

CREATE TABLE IF NOT EXISTS ITEMS (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    DESCRIPTION VARCHAR(1000) NOT NULL,
    IS_AVAILABLE BOOLEAN NOT NULL,
    OWNER_ID BIGINT,
    REQUEST_ID BIGINT,
    CONSTRAINT fk_item_to_user FOREIGN KEY (OWNER_ID) REFERENCES USERS (ID),
    CONSTRAINT fk_item_to_request FOREIGN KEY (REQUEST_ID) REFERENCES REQUESTS (ID)
);

CREATE TABLE IF NOT EXISTS BOOKINGS (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    START_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    END_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    ITEM_ID BIGINT NOT NULL,
    BOOKER_ID BIGINT NOT NULL,
    STATUS VARCHAR(255) NOT NULL,
    CONSTRAINT fk_booking_to_item FOREIGN KEY (ITEM_ID) REFERENCES ITEMS (ID),
    CONSTRAINT fk_booking_to_user FOREIGN KEY (BOOKER_ID) REFERENCES USERS (ID)
);

CREATE TABLE IF NOT EXISTS COMMENTS (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    TEXT VARCHAR(1000) NOT NULL,
    ITEM_ID BIGINT NOT NULL,
    AUTHOR_ID BIGINT NOT NULL,
    CREATED TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_comment_to_item FOREIGN KEY (ITEM_ID) REFERENCES ITEMS (ID),
    CONSTRAINT fk_comment_to_user FOREIGN KEY (AUTHOR_ID) REFERENCES USERS (ID)
);