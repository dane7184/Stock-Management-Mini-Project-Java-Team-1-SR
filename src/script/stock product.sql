CREATE TABLE  if not exists tb_product (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) UNIQUE NOT NULL,
                            unit_price DOUBLE PRECISION NOT NULL CHECK (unit_price >= 0),
                            stock_qty INT NOT NULL CHECK (stock_qty >= 0),
                            import_date DATE
);

INSERT INTO tb_product (name, unit_price, stock_qty)
VALUES ('Computer', 350, 4),
       ('Keyboard', 25, 23),
       ('USB C', 10, 3),
       ('Monitor', 20, 23),
       ('Laptop', 5, 11),
       ('Chacher', 15, 23),
       ('Nokai', 50, 4),
       ('HDMI', 11, 7),
       ('Printer', 9, 82),
       ('Phone Case', 17, 17);

create table if not exists tb_set_row (row int);
insert into tb_set_row values (1);

