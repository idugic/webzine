-- SYSTEM
-- ROLES
INSERT INTO ROLES (CD, NAME) VALUES ('VISITOR', 'Visitor');
INSERT INTO ROLES (CD, NAME) VALUES ('CONTRIBUTOR', 'Contributor');
INSERT INTO ROLES (CD, NAME) VALUES ('EDITOR', 'Editor');
INSERT INTO ROLES (CD, NAME) VALUES ('ADMINISTRATOR', 'Administrator');

-- USER_STATUS
INSERT INTO USER_STATUS (CD, NAME) VALUES ('0', 'Inactive');
INSERT INTO USER_STATUS (CD, NAME) VALUES ('1', 'Active');

-- COUNTRY
INSERT INTO COUNTRY (CD, NAME) VALUES ('RS', 'Serbia');

-- USERS
INSERT INTO USERS (
  USER_NAME,
  PASSWORD,
  STATUS_ID,
  ROLE_ID
) VALUES (
  'admin',
  '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',
  (SELECT ID FROM USER_STATUS WHERE CD = '1'),
  (SELECT ID FROM ROLES WHERE CD = 'ADMINISTRATOR')
);

-- PROJECT MANAGEMENT
-- TASK_STATUS
INSERT INTO TASK_STATUS (CD, NAME) VALUES ('1', 'Active');
INSERT INTO TASK_STATUS (CD, NAME) VALUES ('2', 'Completed');

-- TASK_PRIORITY
INSERT INTO TASK_PRIORITY (CD, NAME) VALUES ('1', 'Normal');
INSERT INTO TASK_PRIORITY (CD, NAME) VALUES ('2', 'High');
INSERT INTO TASK_PRIORITY (CD, NAME) VALUES ('3', 'Low');

-- CONTENT MANAGEMENT
INSERT INTO CONTENT_TYPE (CD, NAME) VALUES ('TEXT', 'Text');
INSERT INTO CONTENT_TYPE (CD, NAME) VALUES ('MEDIA', 'Media');

-- MAGAZINE
-- READER_TYPE
INSERT INTO READER_TYPE (CD, NAME) VALUES ('LK', 'Little Kids (Ages 3-6)');
INSERT INTO READER_TYPE (CD, NAME) VALUES ('K', 'Kids (Ages 7-14)');
INSERT INTO READER_TYPE (CD, NAME) VALUES ('G', 'Grown Ups');

-- ARTICLE_STATUS
INSERT INTO ARTICLE_STATUS (CD, NAME) VALUES ('1', 'Submitted');
INSERT INTO ARTICLE_STATUS (CD, NAME) VALUES ('2', 'Ready');
INSERT INTO ARTICLE_STATUS (CD, NAME) VALUES ('100', 'Published');
