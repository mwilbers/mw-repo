-- #########################################################################
-- 1. create new database user
CREATE USER mwcalendar
    IDENTIFIED BY basic6 
    DEFAULT TABLESPACE MW 
    QUOTA 10M ON MW 
    TEMPORARY TABLESPACE TEMP
    QUOTA 5M ON system;
	
-- 2. grant all necessary privileges
grant all privileges to mwcalendar identified by basic6;