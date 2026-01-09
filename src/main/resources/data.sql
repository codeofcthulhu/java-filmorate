-- ------------------------------------------------------------
-- Initial MPA ratings
-- ------------------------------------------------------------
INSERT INTO mpa_rating (name)
SELECT * FROM (VALUES ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17')) AS new_mpa(name)
WHERE NOT EXISTS (SELECT 1 FROM mpa_rating WHERE mpa_rating.name = new_mpa.name);

-- ------------------------------------------------------------
-- Initial genres
-- ------------------------------------------------------------
INSERT INTO genres (name)
SELECT * FROM (VALUES ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик')) AS new_genres(name)
WHERE NOT EXISTS (SELECT 1 FROM genres WHERE genres.name = new_genres.name);