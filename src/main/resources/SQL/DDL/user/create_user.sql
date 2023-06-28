

SHOW INDEX FROM user ;

CREATE UNIQUE INDEX email_phone_UNIQUE on user (email, phone);

DROP INDEX email_phone_UNIQUE ON user;