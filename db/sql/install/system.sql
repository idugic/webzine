-- ROLES
CREATE TABLE ROLES (
  ID   INTEGER      NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  CD   VARCHAR (15) NOT NULL,
  NAME VARCHAR (50) NOT NULL
);
ALTER TABLE ROLES ADD CONSTRAINT ROLE_PK PRIMARY KEY (ID);
ALTER TABLE ROLES ADD CONSTRAINT ROLE_UK_1 UNIQUE (CD);

-- USERS
CREATE TABLE USERS (
  ID        INTEGER      NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  USER_NAME VARCHAR (50) NOT NULL,
  PASSWORD  VARCHAR (50) NOT NULL,
  STATUS    INTEGER      NOT NULL,
  ROLE_ID   INTEGER      NOT NULL
);
ALTER TABLE USERS ADD CONSTRAINT USER_PK PRIMARY KEY (ID);
ALTER TABLE USERS ADD CONSTRAINT USER_UK_1 UNIQUE (USER_NAME);
ALTER TABLE USERS ADD CONSTRAINT USER_CK_1 CHECK (STATUS IN (0,1));
ALTER TABLE USERS ADD CONSTRAINT USER_ROLE_FK_1 FOREIGN KEY (ROLE_ID) REFERENCES ROLES (ID);

-- COUNTRY
CREATE TABLE COUNTRY (
  ID   INTEGER      NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  CD   VARCHAR (15) NOT NULL,
  NAME VARCHAR (50) NOT NULL
);
ALTER TABLE COUNTRY ADD CONSTRAINT COUN_PK PRIMARY KEY (ID);
ALTER TABLE COUNTRY ADD CONSTRAINT COUN_UK_1 UNIQUE (CD);

-- ADDRESS
CREATE TABLE ADDRESS (
  ID          INTEGER       NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  EMAIL       VARCHAR (100),
  PHONE       VARCHAR (50),
  STREET_LINE VARCHAR (100),
  CITY        VARCHAR (50),
  POSTAL_CODE VARCHAR (15),
  COUNTRY_ID  INTEGER,
  WWW         VARCHAR (250)
);
ALTER TABLE ADDRESS ADD CONSTRAINT ADDR_PK PRIMARY KEY (ID);
ALTER TABLE ADDRESS ADD CONSTRAINT ADDR_COUN_FK_1 FOREIGN KEY (COUNTRY_ID) REFERENCES COUNTRY (ID);

-- USER_PROFILE
CREATE TABLE USER_PROFILE (
  ID         INTEGER      NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  USER_ID    INTEGER      NOT NULL,
  FIRST_NAME VARCHAR (50),
  LAST_NAME  VARCHAR (50),
  BIRTHDAY   DATE,
  ADDRESS_ID INTEGER,
  IMAGE BLOB
);
ALTER TABLE USER_PROFILE ADD CONSTRAINT USER_PROF_PK PRIMARY KEY (ID);
ALTER TABLE USER_PROFILE ADD CONSTRAINT USER_PROF_UK_1 UNIQUE (USER_ID);
ALTER TABLE USER_PROFILE ADD CONSTRAINT USER_PROF_USER_FK_1 FOREIGN KEY (USER_ID) REFERENCES USERS (ID);
ALTER TABLE USER_PROFILE ADD CONSTRAINT USER_PROF_ADDR_FK_1 FOREIGN KEY (ADDRESS_ID) REFERENCES ADDRESS (ID);
