CREATE SCHEMA IF NOT EXISTS product;
CREATE TABLE IF NOT EXISTS product.inventory(
    id uuid DEFAULT gen_random_uuid(),
    version uuid DEFAULT gen_random_uuid(),
    name text NOT NULL,
    versioned_on timestamp DEFAULT now(),
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX index_inventory_name ON product.inventory(name);

TRUNCATE TABLE product.inventory RESTART IDENTITY;

INSERT INTO product.inventory (name) VALUES
 ('Meditations'),
 ('Ascension'),
 ('Science Fiction'),
 ('Free Jazz'),
 ('Compulsion!!!'),
 ('Conquistador!'),
 ('Universal Consciousness'),
 ('Spiritual Unity'),
 ('Interstellar Space'),
 ('The Cecil Taylor Unit'),
 ('Winged Serpent'),
 ('Crystals'),
 ('Unit Structures'),
 ('Staying On The Watch'),
 ('Scorpio'),
 ('Izopho Zam'),
 ('The Jazz Composers Orchestra'),
 ('Blue Notes For Mongezi'),
 ('Expression'),
 ('How Many Clouds Can You See'),
 ('Theoria'),
 ('The Magic City');