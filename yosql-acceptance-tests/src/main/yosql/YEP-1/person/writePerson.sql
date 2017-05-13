--
-- parameters:
--   - name: id
--     type: int
--   - name: name
--     type: java.lang.String
--
INSERT INTO persons (id, name)
    VALUES (:id, :name)
;
