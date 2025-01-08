INSERT IGNORE INTO users (id, user_name, password, name, surname, patronymic, reg_data)
VALUES (1, 'a', '$2a$10$3hdc1zfjZM9Rr8Bc8XL1GezzpKBwTZ0v.DwYMO7.6/QVOwaVZcACq', 'a', 'a', 'a', '2024-11-01');

INSERT IGNORE INTO admins (email, salary, id)
VALUES ('admin@mail.ru', 100000, 1);
