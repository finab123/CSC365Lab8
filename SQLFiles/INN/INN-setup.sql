-- Brandon Newby
-- Cal Poly Fall 2017
-- CSC 365
-- brnewby@calpoly.edu

CREATE TABLE rooms(

  id CHAR(3) PRIMARY KEY,
  name CHAR(40),
  num_beds TINYINT,
  bed_type CHAR(6),
  max_occ TINYINT,
  price INT,
  decor CHAR(20)

);

CREATE TABLE reservations(

  code INT PRIMARY KEY,
  room CHAR(3) REFERENCES rooms(id),
  check_in DATE,
  check_out DATE,
  rate DECIMAL(5,2),
  lName CHAR(30),
  fName CHAR(20),
  adults TINYINT,
  kids TINYINT,

  UNIQUE (room, check_in, check_out)

);
