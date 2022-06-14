CREATE TYPE collaboration_state as ENUM ('active', 'inactive');
CREATE TYPE skill_level as ENUM ('expert', 'average', 'low');

CREATE TABLE IF NOT EXISTS SPRING_SESSION
(
    PRIMARY_ID            CHAR(36) NOT NULL,
    SESSION_ID            CHAR(36) NOT NULL,
    CREATION_TIME         BIGINT   NOT NULL,
    LAST_ACCESS_TIME      BIGINT   NOT NULL,
    MAX_INACTIVE_INTERVAL INT      NOT NULL,
    EXPIRY_TIME           BIGINT   NOT NULL,
    PRINCIPAL_NAME        VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE IF NOT EXISTS SPRING_SESSION_ATTRIBUTES
(
    SESSION_PRIMARY_ID CHAR(36)     NOT NULL,
    ATTRIBUTE_NAME     VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES    BYTEA        NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS student
(
    username      VARCHAR(8)  NOT NULL,
    password      TEXT        NOT NULL,
    balance_hours INTEGER     NOT NULL DEFAULT 0,
    blocked       BOOLEAN     NOT NULL DEFAULT FALSE,
    name          VARCHAR(40) NOT NULL,
    surname       VARCHAR(40) NOT NULL,
    email         VARCHAR(40) NOT NULL UNIQUE,
    street        VARCHAR(40) NOT NULL,
    number        INTEGER     NOT NULL,
    pc            INTEGER     NOT NULL,
    locality      VARCHAR(30) NOT NULL,
    skp           BOOLEAN     NOT NULL DEFAULT FALSE,
    degree        TEXT        NOT NULL,
    CONSTRAINT student_pk PRIMARY KEY (username),
    CONSTRAINT student_pc_ri CHECK (pc > 0),
    CONSTRAINT student_number_ri CHECK (number > 0)
);

CREATE TABLE IF NOT EXISTS skill
(
    name        VARCHAR(30) NOT NULL,
    description VARCHAR(30) NOT NULL,
    level       SKILL_LEVEL NOT NULL,
    canceled    BOOLEAN     NOT NULL DEFAULT FALSE,
    CONSTRAINT skill_pk PRIMARY KEY (name, level)
);

CREATE TABLE IF NOT EXISTS email
(
    id        SERIAL,
    send_date DATE        NOT NULL,
    sender    VARCHAR(40) NOT NULL,
    receiver  VARCHAR(40) NOT NULL,
    subject   VARCHAR(20) NOT NULL,
    body      TEXT        NOT NULL,
    CONSTRAINT email_pk PRIMARY KEY (id),
    CONSTRAINT email_sender_fk FOREIGN KEY (sender) REFERENCES student (email) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT email_receiver_fk FOREIGN KEY (receiver) REFERENCES student (email) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS offer
(
    id          SERIAL,
    name        VARCHAR(30) NOT NULL,
    level       SKILL_LEVEL NOT NULL,
    username    VARCHAR(8)  NOT NULL,
    start_date  DATE        NOT NULL,
    end_date    DATE,
    description TEXT        NOT NULL,
    canceled    BOOLEAN     NOT NULL DEFAULT FALSE,
    CONSTRAINT offer_pk PRIMARY KEY (id),
    CONSTRAINT offer_date_check CHECK (start_date <= end_date OR end_date IS NULL),
    CONSTRAINT offer_student_fk FOREIGN KEY (username) REFERENCES student (username) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT offer_skill_fk FOREIGN KEY (name, level) REFERENCES skill (name, level) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS request
(
    id          SERIAL,
    name        VARCHAR(30) NOT NULL,
    level       SKILL_LEVEL NOT NULL,
    username    VARCHAR(8)  NOT NULL,
    start_date  DATE        NOT NULL,
    end_date    DATE,
    description TEXT        NOT NULL,
    canceled    BOOLEAN     NOT NULL DEFAULT FALSE,
    CONSTRAINT request_pk PRIMARY KEY (id),
    CONSTRAINT request_date_ri CHECK (start_date <= end_date OR end_date IS NULL),
    CONSTRAINT request_student_fk FOREIGN KEY (username) REFERENCES student (username) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT request_skill_fk FOREIGN KEY (name, level) REFERENCES skill (name, level) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS collaboration
(
    id_offer   INTEGER             NOT NULL,
    id_request INTEGER             NOT NULL,
    hours      INTEGER             NOT NULL DEFAULT 0,
    assessment INTEGER             NOT NULL DEFAULT 1,
    state      COLLABORATION_STATE NOT NULL DEFAULT 'active',
    CONSTRAINT collaboration_pk PRIMARY KEY (id_offer, id_request),
    CONSTRAINT collaboration_fk_offer FOREIGN KEY (id_offer) REFERENCES offer (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT collaboration_fk_request FOREIGN KEY (id_request) REFERENCES request (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT collaboration_hours_ri CHECK (hours >= 0),
    CONSTRAINT collaboration_assessment_ri CHECK (assessment > 0 AND assessment <= 5)
);

CREATE TABLE IF NOT EXISTS message
(
    username1 VARCHAR(8) NOT NULL,
    username2 VARCHAR(8) NOT NULL,
    date_time TIMESTAMP  NOT NULL,
    text      TEXT       NOT NULL,
    CONSTRAINT message_pk PRIMARY KEY (username1, username2, date_time),
    CONSTRAINT message_username1_fk FOREIGN KEY (username1) REFERENCES student (username) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT message_username2_fk FOREIGN KEY (username2) REFERENCES student (username) ON DELETE RESTRICT ON UPDATE CASCADE
);
