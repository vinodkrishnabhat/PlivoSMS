CREATE TABLE sms_count (
    id serial NOT NULL,
    from_number character varying(40),
    start_time timestamp,
    counter int
);
create unique index smscount_fromnumber_unique on sms_count (from_number);

CREATE TABLE blocked_pair (
    id serial NOT NULL,
    from_number character varying(40),
    to_number character varying(40),
    set_at_time bigint
);

insert into account (auth_id, username) values ('password', 'admin');