USE master
GO

--drop database if it exists
IF DB_ID('final_capstone') IS NOT NULL
BEGIN
	ALTER DATABASE final_capstone SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
	DROP DATABASE final_capstone;
END

CREATE DATABASE final_capstone
GO

USE final_capstone
GO

--create tables
CREATE TABLE users (
	user_id int IDENTITY(1,1) NOT NULL,
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	salt varchar(200) NOT NULL,
	user_role varchar(50) NOT NULL
	CONSTRAINT PK_user PRIMARY KEY (user_id)
)

CREATE TABLE campaign (
    campaign_id int IDENTITY(1,1) NOT NULL,
    campaign_name varchar(50) NOT NULL,
    description varchar(500) NOT NULL,
    funding_goal integer NOT NULL,
    start_date datetime NOT NULL,
    end_date datetime NOT NULL,
    locked BIT default 0 NOT NULL, --TODO: must be locked if there's outstanding spend requests
    isPublic BIT default 0 NOT NULL,
    deleted BIT default 0 NOT NULL,

    CONSTRAINT pk_campaign PRIMARY KEY (campaign_id),
    CONSTRAINT valid_funding_goal CHECK (funding_goal >= 100),
    --TODO: maybe modify this constraint
    --end date must be at least 24 hours after start date
    --CONSTRAINT valid_dates_end_after_start
    --    CHECK (EXTRACT (EPOCH FROM end_date) - EXTRACT (EPOCH FROM start_date) >= 86400),
    CONSTRAINT locked_if_deleted
        CHECK (deleted <> 1 OR locked <> 1)
);

--populate default data
INSERT INTO users (username, password_hash, salt, user_role) VALUES ('user','Jg45HuwT7PZkfuKTz6IB90CtWY4=','LHxP4Xh7bN0=','user');
INSERT INTO users (username, password_hash, salt, user_role) VALUES ('admin','YhyGVQ+Ch69n4JMBncM4lNF/i9s=', 'Ar/aB2thQTI=','admin');

GO