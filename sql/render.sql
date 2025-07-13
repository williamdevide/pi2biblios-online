-- Database: pi2biblios_ea33

-- DROP DATABASE IF EXISTS pi2biblios_ea33;

CREATE DATABASE pi2biblios_ea33
    WITH
    OWNER = pi2biblios_ea33_user
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF8'
    LC_CTYPE = 'en_US.UTF8'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

ALTER DATABASE pi2biblios_ea33
    SET "TimeZone" TO 'utc';

ALTER DEFAULT PRIVILEGES FOR ROLE postgres
GRANT ALL ON TABLES TO pi2biblios_ea33_user;

ALTER DEFAULT PRIVILEGES FOR ROLE postgres
GRANT ALL ON SEQUENCES TO pi2biblios_ea33_user;

ALTER DEFAULT PRIVILEGES FOR ROLE postgres
GRANT EXECUTE ON FUNCTIONS TO pi2biblios_ea33_user;

ALTER DEFAULT PRIVILEGES FOR ROLE postgres
GRANT USAGE ON TYPES TO pi2biblios_ea33_user;