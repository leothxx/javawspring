CREATE TABLE good (
	idx int not null AUTO_INCREMENT PRIMARY KEY, /* 좋아요 테이블 고유번호 */
	part varchar(20) not null, /*  어떤 파트 */
	partIdx int not null, /* 해당 파트 게시물의 고유번호 */
	mid varchar(20) not null, /* 해달 분야의 해당 게시글에 접속한 사용자의 아이디 */
	good char(1) default 'Y' /* 좋아요(Y)/좋아요해제(N) */
);

DESC good;

DROP TABLE good;