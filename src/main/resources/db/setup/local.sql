CREATE DATABASE izen;

CREATE DATABASE izen_test;

CREATE USER test_user WITH PASSWORD 'test1234';

\c izen_test

GRANT ALL PRIVILEGES ON DATABASE izen_test TO test_user;