-- Sequences
DROP SEQUENCE IF EXISTS flight_seq CASCADE;
DROP SEQUENCE IF EXISTS review_seq CASCADE;
DROP SEQUENCE IF EXISTS response_seq CASCADE;
DROP SEQUENCE IF EXISTS dbuser_seq CASCADE;

CREATE SEQUENCE flight_seq START 1;
CREATE SEQUENCE review_seq START 1;
CREATE SEQUENCE response_seq START 1;
CREATE SEQUENCE dbuser_seq START 1;

-- Table: dbuser
DROP TABLE IF EXISTS public.dbuser;

CREATE TABLE IF NOT EXISTS public.dbuser (
                                             id BIGINT NOT NULL DEFAULT nextval('dbuser_seq'::regclass),
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    CONSTRAINT dbuser_pkey PRIMARY KEY (id),
    CONSTRAINT dbuser_username_key UNIQUE (username)
    );

-- Table: flight
DROP TABLE IF EXISTS public.flight;

CREATE TABLE IF NOT EXISTS public.flight (
                                             id BIGINT NOT NULL DEFAULT nextval('flight_seq'::regclass),
    flight_number VARCHAR(255) NOT NULL,
    flight_date DATE NOT NULL,
    airline VARCHAR(255) NOT NULL,
    CONSTRAINT flight_pkey PRIMARY KEY (id)
    );

-- Table: review
DROP TABLE IF EXISTS public.review;

CREATE TABLE IF NOT EXISTS public.review (
                                             id BIGINT NOT NULL DEFAULT nextval('review_seq'::regclass),
    reference VARCHAR(255),
    rating INTEGER,
    comment TEXT,
    review_date DATE,
    reviewer_name VARCHAR(255),
    status VARCHAR(255),
    flight_id BIGINT,
    writer_name VARCHAR(255),
    dbuser_id BIGINT,
    CONSTRAINT review_pkey PRIMARY KEY (id),
    CONSTRAINT fk_review_flight FOREIGN KEY (flight_id) REFERENCES public.flight (id) ON DELETE CASCADE,
    CONSTRAINT fk_review_dbuser FOREIGN KEY (dbuser_id) REFERENCES public.dbuser (id) ON DELETE CASCADE
    );

-- Table: response
DROP TABLE IF EXISTS public.response;

CREATE TABLE IF NOT EXISTS public.response (
                                               id BIGINT NOT NULL DEFAULT nextval('response_seq'::regclass),
    content TEXT,
    response_date DATE,
    review_id BIGINT UNIQUE,
    outcome VARCHAR(255),
    dbuser_id BIGINT,
    CONSTRAINT response_pkey PRIMARY KEY (id),
    CONSTRAINT fk_response_review FOREIGN KEY (review_id) REFERENCES public.review (id) ON DELETE CASCADE,
    CONSTRAINT fk_response_dbuser FOREIGN KEY (dbuser_id) REFERENCES public.dbuser (id) ON DELETE CASCADE
    );

INSERT INTO flight (flight_number, flight_date, airline) VALUES
                                                             ('AF123', '2025-07-10', 'Air France'),
                                                             ('KL456', '2025-07-11', 'KLM'),
                                                             ('AF789', '2025-07-12', 'Air France'),
                                                             ('KL101', '2025-07-13', 'KLM'),
                                                             ('AF202', '2025-07-14', 'Air France'),
                                                             ('KL303', '2025-07-15', 'KLM'),
                                                             ('AF404', '2025-07-16', 'Air France'),
                                                             ('KL505', '2025-07-17', 'KLM'),
                                                             ('LH808', '2025-07-18', 'Lufthansa'),
                                                             ('AF222', '2025-07-21', 'Air France'),
                                                             ('LH333', '2025-07-22', 'Lufthansa');

INSERT INTO dbuser (username, password, role) VALUES
                                                  ('admin1', 'adminPass123', 'ADMIN'),
                                                  ('client1', 'clientPass123', 'CLIENT'),
                                                  ('client2', 'clientPass456', 'CLIENT');

ALTER TABLE public.flight OWNER TO postgres;
ALTER TABLE public.review OWNER TO postgres;
ALTER TABLE public.response OWNER TO postgres;
ALTER TABLE public.dbuser OWNER TO postgres;

GRANT ALL ON TABLE public.flight TO postgres;
GRANT ALL ON TABLE public.review TO postgres;
GRANT ALL ON TABLE public.response TO postgres;
GRANT ALL ON TABLE public.dbuser TO postgres;