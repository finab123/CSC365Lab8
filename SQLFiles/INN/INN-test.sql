--
-- CSC 365. Lab 2. Test script for INN dataset

-- Brandon Newby
-- Cal Poly Fall 2017
-- CSC 365
-- brnewby@calpoly.edu
-- 

-- set the destination for the output to lab2-INN-output.txt
tee lab2-INN-output.txt

-- Note: \! will make an operating system call, in the line below, to echo.
-- The echo'd text will not appear in the tee file.
\! echo "select database";
use brnewby;  -- put your database here; replace 'evillarr' with your login


-- see what tables there are
\! echo '***********************************************'
\! echo '        Let us see what tables there are       '
\! echo '***********************************************'
show tables; 


-- Create tables
# '***********************************************'
# '              Creating tables                  '
# '***********************************************'
source INN-setup.sql

\! echo '***********************************************'
\! echo '       Now let us see what tables there are    '
\! echo '***********************************************'
show tables; 


-- Here you will put a line for each build script you have.
\! echo '***********************************************'
\! echo '         Populating tables                     '
\! echo '***********************************************'
source INN-build-rooms.sql
source INN-build-reservations.sql


-- To count the number of tuples in each of your tables, 
-- uncomment the following lines. Copy the SELECT statement
-- for each of your tables and replace the <my-INN-table>
-- with your own table names.
--\! echo '***********************************************'
--\! echo '             Counting tuples                   '
--\! echo '***********************************************'
--SELECT count(*) from <my-INN-table>;
SELECT count(*) from rooms;
SELECT count(*) from reservations;

SELECT * from rooms;
SELECT * from reservations;

-- If you want, you can also display the tuples. Just add
-- whatever SELECT statements you wish, changing INN to the
-- appropriate table name.
-- SELECT * FROM INN;


\! echo '***********************************************'
\! echo '              Dropping tables                  '
\! echo '***********************************************'
source INN-cleanup.sql


\! echo '***********************************************'
\! echo '      See if the tables were dropped           '
\! echo '***********************************************'
show tables;


\! echo '***********************************************'
\! echo '        All done.     YEE-HAW!                 '
\! echo '***********************************************'

notee
