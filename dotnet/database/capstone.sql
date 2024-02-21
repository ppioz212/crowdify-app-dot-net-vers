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
        CHECK (deleted <> 1 OR locked = 1)
);

CREATE TABLE campaign_manager (
    campaign_id integer,
    manager_id integer,
    creator BIT DEFAULT 1 NOT NULL, --TODO: add constraint only non-creator managers can be removed, also if creator already exists default FALSE

    CONSTRAINT pk_campaign_manager PRIMARY KEY (campaign_id, manager_id),
    CONSTRAINT fk_campaign_id FOREIGN KEY (campaign_id) REFERENCES campaign (campaign_id),
    CONSTRAINT fk_manager_user_id FOREIGN KEY (manager_id) REFERENCES users (user_id)
);


CREATE TABLE donation (
    donation_id int IDENTITY(1,1) NOT NULL,
    donor_id integer,
    campaign_id integer NOT NULL,
    donation_amount integer NOT NULL,
    donation_date datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    donation_comment varchar(200),
    refunded BIT DEFAULT 0 NOT NULL,
    anonymous BIT NOT NULL,

    CONSTRAINT pk_donation_id PRIMARY KEY (donation_id),
    CONSTRAINT fk_campaign_id_2 FOREIGN KEY (campaign_id) REFERENCES campaign (campaign_id),
    CONSTRAINT fk_donor_user_id FOREIGN KEY (donor_id) REFERENCES users (user_id),
    CONSTRAINT valid_donation_amount CHECK (donation_amount > 0 AND donation_amount <= 50000000),
    CONSTRAINT anonymous_if_null_donor_id CHECK (donor_id IS NOT null OR anonymous = 1)
);

--populate default data
INSERT INTO users (username, password_hash, salt, user_role) VALUES ('user','Jg45HuwT7PZkfuKTz6IB90CtWY4=','LHxP4Xh7bN0=','user');
INSERT INTO users (username, password_hash, salt, user_role) VALUES ('admin','YhyGVQ+Ch69n4JMBncM4lNF/i9s=', 'Ar/aB2thQTI=','admin');


GO