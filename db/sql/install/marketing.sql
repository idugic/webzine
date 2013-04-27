-- ADVERTISER
CREATE TABLE ADVERTISER ( 
  ID          INTEGER       NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  NAME        VARCHAR (100) NOT NULL, 
  DESCRIPTION VARCHAR (500), 
  ADDRESS_ID  INTEGER 
);
ALTER TABLE ADVERTISER ADD CONSTRAINT ADVE_PK PRIMARY KEY (ID);
ALTER TABLE ADVERTISER ADD CONSTRAINT ADVE_ADDR_FK_1 FOREIGN KEY (ADDRESS_ID) REFERENCES ADDRESS (ID);

-- AD_STATUS
CREATE TABLE AD_STATUS (
  ID   INTEGER      NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  CD   VARCHAR (15) NOT NULL , 
  NAME VARCHAR (50) NOT NULL 
);
ALTER TABLE AD_STATUS ADD CONSTRAINT AD_STAT_PK PRIMARY KEY (ID);
ALTER TABLE AD_STATUS ADD CONSTRAINT AD_STAT_UK_1 UNIQUE (CD);

-- AD
CREATE TABLE AD ( 
  ID                 INTEGER       NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  STATUS_ID          INTEGER       NOT NULL, 
  ADVERTISER_ID      INTEGER       NOT NULL, 
  NAME               VARCHAR (100) NOT NULL, 
  DESCRIPTION        VARCHAR (500), 
  MANAGED_CONTENT_ID INTEGER       NOT NULL, 
  UC                 INTEGER           NOT NULL,
  DC                 TIMESTAMP         NOT NULL,
  UM                 INTEGER,
  DM                 TIMESTAMP
);
ALTER TABLE AD ADD CONSTRAINT AD_PK PRIMARY KEY (ID) ;
ALTER TABLE AD ADD CONSTRAINT AD_STAT_FK_1 FOREIGN KEY (STATUS_ID) REFERENCES AD_STATUS (ID);
ALTER TABLE AD ADD CONSTRAINT AD_ADVE_FK_1 FOREIGN KEY (ADVERTISER_ID) REFERENCES ADVERTISER (ID);
ALTER TABLE AD ADD CONSTRAINT AD_MANA_CONT_FK_1 FOREIGN KEY (MANAGED_CONTENT_ID) REFERENCES MANAGED_CONTENT (ID);
ALTER TABLE AD ADD CONSTRAINT AD_USER_FK_1 FOREIGN KEY (UC) REFERENCES USERS (ID);
ALTER TABLE AD ADD CONSTRAINT AD_USER_FK_2 FOREIGN KEY (UM) REFERENCES USERS (ID);

-- AD_ARTICLE
CREATE TABLE AD_ARTICLE ( 
  ID         INTEGER   NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  AD_ID      INTEGER   NOT NULL, 
  ARTICLE_ID INTEGER   NOT NULL, 
  UC         INTEGER   NOT NULL, 
  DC         TIMESTAMP NOT NULL, 
  UM         INTEGER, 
  DM         TIMESTAMP 
);
ALTER TABLE AD_ARTICLE ADD CONSTRAINT AD_ARTI_PK PRIMARY KEY (ID);
ALTER TABLE AD_ARTICLE ADD CONSTRAINT AD_ARTI_AD_FK_1 FOREIGN KEY (AD_ID) REFERENCES AD (ID);
ALTER TABLE AD_ARTICLE ADD CONSTRAINT AD_ARTI_ARTI_FK_1 FOREIGN KEY (ARTICLE_ID) REFERENCES ARTICLE (ID);
ALTER TABLE AD_ARTICLE ADD CONSTRAINT AD_ARTI_USER_FK_1 FOREIGN KEY (UC) REFERENCES USERS (ID);
ALTER TABLE AD_ARTICLE ADD CONSTRAINT AD_ARTI_USER_FK_2 FOREIGN KEY (UM) REFERENCES USERS (ID);
