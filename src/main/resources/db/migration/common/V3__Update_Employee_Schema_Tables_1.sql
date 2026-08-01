ALTER TABLE auth.account
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE auth.account
    ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE employee.profile
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE employee.profile
    ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE employee.attendance
    ADD COLUMN created_by BIGINT;
ALTER TABLE employee.attendance
    ADD COLUMN updated_by BIGINT;
ALTER TABLE employee.attendance
    ADD COLUMN
        created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL
            DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE employee.attendance
    ADD COLUMN updated_at TIMESTAMP(6) WITH TIME ZONE NOT NULL
        DEFAULT CURRENT_TIMESTAMP;
UPDATE employee.attendance
SET created_by = 1,
    updated_by = 1;
ALTER TABLE employee.attendance
    ALTER COLUMN created_by SET NOT NULL;
ALTER TABLE employee.attendance
    ALTER COLUMN updated_by SET NOT NULL;

ALTER TABLE employee.salary
    ADD COLUMN created_by BIGINT;
ALTER TABLE employee.salary
    ADD COLUMN updated_by BIGINT;
ALTER TABLE employee.salary
    ADD COLUMN
        created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL
            DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE employee.salary
    ADD COLUMN updated_at TIMESTAMP(6) WITH TIME ZONE NOT NULL
        DEFAULT CURRENT_TIMESTAMP;
UPDATE employee.salary
SET created_by = 1,
    updated_by = 1;
ALTER TABLE employee.salary
    ALTER COLUMN created_by SET NOT NULL;
ALTER TABLE employee.salary
    ALTER COLUMN updated_by SET NOT NULL;
