SET
check_function_bodies = false;

CREATE TABLE file
(
    id         uuid      NOT null DEFAULT uuid_generate_v4(),
    "name"     varchar   NOT NULL,
    created_at timestamp NOT NULL,
    CONSTRAINT file_pkey PRIMARY KEY (id)
);

CREATE INDEX file_idx ON file;

CREATE TABLE chunks_processing
(
    id                     uuid      NOT null DEFAULT uuid_generate_v4(),
    file_id                uuid      NOT NULL,
    processing_statuses_id integer   NOT NULL,
    first_line             integer   NOT NULL,
    last_line              integer   NOT NULL,
    created_at             timestamp NOT NULL,
    updated_at             timestamp,
    CONSTRAINT chunks_processing_pkey PRIMARY KEY (id, file_id)
);

CREATE TABLE processing_statuses
(
    id          integer   NOT NULL,
    "name"      varchar   NOT NULL,
    description varchar   NOT NULL,
    created_at  timestamp NOT NULL,
    CONSTRAINT processing_statuses_pkey PRIMARY KEY (id)
);

ALTER TABLE chunks_processing
    ADD CONSTRAINT chunks_processing_file_id_fkey
        FOREIGN KEY (file_id) REFERENCES file (id);

ALTER TABLE chunks_processing
    ADD CONSTRAINT chunks_processing_processing_statuses_id_fkey
        FOREIGN KEY (processing_statuses_id) REFERENCES processing_statuses (id);
