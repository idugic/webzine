-- TASK
CREATE TABLE TASK (
  ID             INTEGER       NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  STATUS         INTEGER       NOT NULL,
  PRIORITY       INTEGER       NOT NULL,
  TITLE          VARCHAR (250) NOT NULL,
  DESCRIPTION    VARCHAR (1000),
  OWNER_USER_ID  INTEGER,
  PARENT_TASK_ID INTEGER,
  UC             INTEGER       NOT NULL,
  DC             TIMESTAMP     NOT NULL,
  UM             INTEGER,
  DM             TIMESTAMP
);
ALTER TABLE TASK ADD CONSTRAINT TASK_PK PRIMARY KEY (ID);
ALTER TABLE TASK ADD CONSTRAINT TASK_CK_1 CHECK (STATUS IN (1,100));
ALTER TABLE TASK ADD CONSTRAINT TASK_CK_2 CHECK (PRIORITY IN (1,2,3));
ALTER TABLE TASK ADD CONSTRAINT TASK_TASK_FK_1 FOREIGN KEY (PARENT_TASK_ID) REFERENCES TASK (ID);
ALTER TABLE TASK ADD CONSTRAINT TASK_USER_FK_1 FOREIGN KEY (UM) REFERENCES USERS (ID);
ALTER TABLE TASK ADD CONSTRAINT TASK_USER_FK_2 FOREIGN KEY (OWNER_USER_ID) REFERENCES USERS (ID);
ALTER TABLE TASK ADD CONSTRAINT TASK_USER_FK_3 FOREIGN KEY (UC) REFERENCES USERS (ID);

-- TASK_ATTACHMENT
CREATE TABLE TASK_ATTACHMENT (
  ID      INTEGER       NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  TASK_ID INTEGER       NOT NULL,
  NAME    VARCHAR (100) NOT NULL,
  CONTENT BLOB          NOT NULL,
  UC INTEGER            NOT NULL,
  DC TIMESTAMP          NOT NULL,
  UM INTEGER,
  DM TIMESTAMP
);
ALTER TABLE TASK_ATTACHMENT ADD CONSTRAINT TASK_ATTA_PK PRIMARY KEY (ID);
ALTER TABLE TASK_ATTACHMENT ADD CONSTRAINT TASK_ATTA_TASK_FK_1 FOREIGN KEY (TASK_ID) REFERENCES TASK (ID);
ALTER TABLE TASK_ATTACHMENT ADD CONSTRAINT TASK_ATTA_USER_FK_1 FOREIGN KEY (UC) REFERENCES USERS (ID);
ALTER TABLE TASK_ATTACHMENT ADD CONSTRAINT TASK_ATTA_USER_FK_2 FOREIGN KEY (UM) REFERENCES USERS (ID);

-- TASK_COMMENT
CREATE TABLE TASK_COMMENT (
  ID      INTEGER       NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  TASK_ID INTEGER       NOT NULL,
  TEXT    VARCHAR (250) NOT NULL,
  UC      INTEGER       NOT NULL,
  DC      TIMESTAMP     NOT NULL,
  UM      INTEGER,
  DM      TIMESTAMP
);
ALTER TABLE TASK_COMMENT ADD CONSTRAINT TASK_COMM_PK PRIMARY KEY (ID);
ALTER TABLE TASK_COMMENT ADD CONSTRAINT TASK_COMM_TASK_FK_1 FOREIGN KEY (TASK_ID) REFERENCES TASK (ID);
ALTER TABLE TASK_COMMENT ADD CONSTRAINT TASK_COMM_USER_FK_1 FOREIGN KEY (UM) REFERENCES USERS (ID);
ALTER TABLE TASK_COMMENT ADD CONSTRAINT TASK_COMM_USER_FK_2 FOREIGN KEY (UC) REFERENCES USERS (ID);
