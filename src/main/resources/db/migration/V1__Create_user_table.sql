create table USER
(
    ID           INT auto_increment,
    NAME         VARCHAR(100),
    ACCOUNT_ID   VARCHAR(30),
    TOKEN        CHAR(36),
    GMT_CREATE   BIGINT,
    GMT_MODIFIED BIGINT,
    constraint USER_PK
        primary key (ID)
);