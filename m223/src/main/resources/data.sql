INSERT INTO USER (name, lastname, email, password, role)
VALUES ('Rutschmann', 'Peter,' 'peter.Rutschmann@gmail.com', 'test1234', 'admin'),
        ('Höfflinger', 'Johannes,' 'johannes.höfflinger@gmail.com', 'test1234', 'member');

INSERT INTO BOOKING (user, date, day_duration)
VALUES (1, '2022-09-23', 0.5);
