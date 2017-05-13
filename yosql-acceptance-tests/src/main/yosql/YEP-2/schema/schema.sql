-- name: dropTables
DROP TABLE IF EXISTS persons
;

-- name: createTables
CREATE TABLE persons (
    id INTEGER,
    name VARCHAR(50)
)
;
