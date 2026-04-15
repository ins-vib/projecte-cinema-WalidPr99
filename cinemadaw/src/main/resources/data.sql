-- =============================================
-- CINEMAS (name: 2-110 chars, address: 5-150 chars, city: 2-150 chars, postalCode: 5 dígits)
-- =============================================
INSERT INTO CINEMA(ADDRESS,CITY,NAME,POSTAL_CODE) VALUES
('Carrer Major, 15','Tarragona','Oscars Cinema','43100');

INSERT INTO CINEMA(ADDRESS,CITY,NAME,POSTAL_CODE) VALUES
('Gran Via, 25','Barcelona','Cineplex','08001');

INSERT INTO CINEMA(ADDRESS,CITY,NAME,POSTAL_CODE) VALUES
('Calle Mayor, 8','Madrid','Cinema Palace','28013');

INSERT INTO CINEMA(ADDRESS,CITY,NAME,POSTAL_CODE) VALUES
('Rambla Nova, 30','Tarragona','Multicines Odeon','43001');

INSERT INTO CINEMA(ADDRESS,CITY,NAME,POSTAL_CODE) VALUES
('Paseo de Gracia, 42','Barcelona','Royal Cinema','08007');

INSERT INTO CINEMA(ADDRESS,CITY,NAME,POSTAL_CODE) VALUES
('Calle Colón, 18','Valencia','Cines Capitol','46004');

INSERT INTO CINEMA(ADDRESS,CITY,NAME,POSTAL_CODE) VALUES
('Alameda Principal, 50','Málaga','Cines Albéniz','29001');

INSERT INTO CINEMA(ADDRESS,CITY,NAME,POSTAL_CODE) VALUES
('Gran Vía, 33','Madrid','Callao City Lights','28013');

INSERT INTO CINEMA(ADDRESS,CITY,NAME,POSTAL_CODE) VALUES
('Calle Sierpes, 12','Sevilla','Cines Avenida','41004');

INSERT INTO CINEMA(ADDRESS,CITY,NAME,POSTAL_CODE) VALUES
('Ronda Universitat, 7','Barcelona','Verdi Park','08007');

-- =============================================
-- ROOMS (name: 2-110 chars, capacity: 1-500)
-- =============================================
INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES
('Sala 1',120,1);
INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES
('Sala 2',120,1);
INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES
('Sala 3',120,1);

INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES
('Sala 1',150,2);
INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES
('Sala 2',100,2);
INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES
('Sala 3',80,2);

INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES
('Sala 1',200,3);
INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES
('Sala 2',180,3);
INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES
('Sala 3',90,3);

-- =============================================
-- MOVIES (title: 2-110 chars, duration: 1-500, genre: 2-110 chars, description: 10-500 chars, releaseDate: yyyy-MM-dd)
-- =============================================
INSERT INTO MOVIE(TITLE,DURATION,GENRE,DESCRIPTION,RELEASE_DATE) VALUES
('El Padrino',175,'Drama','La historia de la familia Corleone en el mundo de la mafia de Nueva York','1972-03-24');

INSERT INTO MOVIE(TITLE,DURATION,GENRE,DESCRIPTION,RELEASE_DATE) VALUES
('Inception',148,'Ciencia ficción','Un ladrón que roba secretos corporativos a través de la tecnología de los sueños compartidos','2010-07-16');

INSERT INTO MOVIE(TITLE,DURATION,GENRE,DESCRIPTION,RELEASE_DATE) VALUES
('Interstellar',169,'Ciencia ficción','Un grupo de exploradores viaja a través de un agujero de gusano en busca de un nuevo hogar para la humanidad','2014-11-07');

INSERT INTO MOVIE(TITLE,DURATION,GENRE,DESCRIPTION,RELEASE_DATE) VALUES
('Pulp Fiction',154,'Thriller','Las vidas de dos mafiosos, un boxeador y una pareja de ladrones se entrelazan en historias de violencia y redención','1994-10-14');

INSERT INTO MOVIE(TITLE,DURATION,GENRE,DESCRIPTION,RELEASE_DATE) VALUES
('The Dark Knight',152,'Acción','Batman se enfrenta al Joker, un criminal que siembra el caos y la destrucción en la ciudad de Gotham','2008-07-18');

-- =============================================
-- SEATS (seatRow: 1-10 chars, number: 1-500, type: Standard/Premium/Adapted, state: true/false)
-- =============================================
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('A',1,0,0,'Standard',true,1);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('A',2,1,0,'Standard',true,1);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('A',3,2,0,'Standard',true,1);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('A',4,3,0,'Premium',true,1);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('A',5,4,0,'Premium',true,1);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('B',1,0,1,'Standard',true,1);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('B',2,1,1,'Standard',true,1);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('B',3,2,1,'Adapted',true,1);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('B',4,3,1,'Standard',true,1);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('B',5,4,1,'Standard',true,1);

INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('A',1,0,0,'Standard',true,2);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('A',2,1,0,'Standard',true,2);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('A',3,2,0,'Premium',true,2);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('B',1,0,1,'Standard',true,2);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('B',2,1,1,'Adapted',true,2);

INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('A',1,0,0,'Standard',true,3);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('A',2,1,0,'Standard',true,3);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('A',3,2,0,'Standard',true,3);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('B',1,0,1,'Premium',true,3);
INSERT INTO SEAT(SEAT_ROW,NUMBER,X,Y,TYPE,STATE,ROOM_ID) VALUES
('B',2,1,1,'Standard',true,3);

-- =============================================
-- SCREENINGS (dateTime: yyyy-MM-dd HH:mm:ss, price, movie, room)
-- =============================================
INSERT INTO SCREENING(DATE_TIME,PRICE,MOVIE_ID,ROOM_ID) VALUES
('2026-03-25 18:00:00',8.50,1,1);

INSERT INTO SCREENING(DATE_TIME,PRICE,MOVIE_ID,ROOM_ID) VALUES
('2026-03-25 21:30:00',9.50,1,2);

INSERT INTO SCREENING(DATE_TIME,PRICE,MOVIE_ID,ROOM_ID) VALUES
('2026-03-26 19:15:00',8.00,2,4);

INSERT INTO SCREENING(DATE_TIME,PRICE,MOVIE_ID,ROOM_ID) VALUES
('2026-03-27 20:00:00',10.00,3,7);

-- Projeccio d'avui (per comprovar que demà ja no es pot reservar)
INSERT INTO SCREENING(DATE_TIME,PRICE,MOVIE_ID,ROOM_ID) VALUES
('2026-04-15 18:30:00',8.50,1,1);

-- Noves projeccions futures reservables
INSERT INTO SCREENING(DATE_TIME,PRICE,MOVIE_ID,ROOM_ID) VALUES
('2026-04-18 20:00:00',9.00,1,2);

INSERT INTO SCREENING(DATE_TIME,PRICE,MOVIE_ID,ROOM_ID) VALUES
('2026-04-19 19:30:00',8.75,2,4);

INSERT INTO SCREENING(DATE_TIME,PRICE,MOVIE_ID,ROOM_ID) VALUES
('2026-04-21 21:00:00',9.25,2,5);

INSERT INTO SCREENING(DATE_TIME,PRICE,MOVIE_ID,ROOM_ID) VALUES
('2026-04-20 20:15:00',10.00,3,7);

INSERT INTO SCREENING(DATE_TIME,PRICE,MOVIE_ID,ROOM_ID) VALUES
('2026-04-22 18:45:00',7.90,4,3);

INSERT INTO SCREENING(DATE_TIME,PRICE,MOVIE_ID,ROOM_ID) VALUES
('2026-04-23 22:00:00',8.20,4,6);

INSERT INTO SCREENING(DATE_TIME,PRICE,MOVIE_ID,ROOM_ID) VALUES
('2026-04-24 21:30:00',9.80,5,8);

INSERT INTO SCREENING(DATE_TIME,PRICE,MOVIE_ID,ROOM_ID) VALUES
('2026-04-26 18:00:00',8.60,5,9);

