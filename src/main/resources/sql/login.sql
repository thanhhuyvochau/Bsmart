DELIMITER $$
DROP PROCEDURE IF EXISTS CheckFirstLogin $$
CREATE PROCEDURE CheckFirstLogin(IN email VARCHAR(255))
BEGIN
    SELECT is_first_login FROM user WHERE email = email limit 1;
END;

CALL CheckFirstLogin('nhatgv5@gmail.com');