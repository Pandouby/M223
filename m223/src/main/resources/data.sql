INSERT INTO MEMBER (id, name, lastname, email, password, role)
VALUES (1, 'Peter', 'Rutschmann', 'peter.rutschmann@gmail.com', 'test1234', 'ADMIN'),
       (2, 'Johannes', 'Höffliger', 'johannes.höffliger@gmail.com', 'test1234', 'MEMBER');

INSERT INTO BOOKING (id, creator, day_duration, date, status)
VALUES (1, 1, 0.5, '2022-09-23', 'Pending');