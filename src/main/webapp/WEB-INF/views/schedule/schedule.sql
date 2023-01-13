show tables;

CREATE TABLE schedule2 (
	idx INT NOT NULL AUTO_INCREMENT PRIMARY KEY, /* 일정 고유번호 */
	mid VARCHAR(20) NOT NULL, /* 유저 아이디 */
	sDate DATETIME NOT NULL, /* 일정을 등록한 날짜 */
	part VARCHAR(20) NOT NULL, /* 1.모임 2.업무 3.학습 4.여행 0.기타 */
	content TEXT NOT NULL /* 일정 상세 내용 */
);

DESC schedule2;

INSERT INTO schedule2 VALUES (default, 'admin','2023-01-25','여행','일본 유니버셜 스튜디오 재팬 방문');
INSERT INTO schedule2 VALUES (default, 'admin','2023-01-26','여행','일본 오사카조, 교토 여행');
INSERT INTO schedule2 VALUES (default, 'admin','2023-01-27','여행','일본 효고현 고베시 여행');
INSERT INTO schedule2 VALUES (default, 'admin','2023-01-28','여행','일본 오사카 여행');
INSERT INTO schedule2 VALUES (default, 'admin','2023-01-22','모임','설 연휴 외가집 방문');
INSERT INTO schedule2 VALUES (default, 'leothx_x','2023-01-13','모임','이준희씨 생일파티모임');
INSERT INTO schedule2 VALUES (default, 'leothx_x','2023-01-15','모임','오전에 바이크 드라이브');
INSERT INTO schedule2 VALUES (default, 'leothx_x','2023-01-13','모임','이준희씨 생일파티모임2');
INSERT INTO schedule2 VALUES (default, 'leothx_x','2023-01-14','업무','프로젝트 준비 및 구상');
INSERT INTO schedule2 VALUES (default, 'leothx_x','2023-01-14','학습','주말간 과제 처리');
INSERT INTO schedule2 VALUES (default, 'leothx_x','2023-01-15','기타','오송역 마중');
INSERT INTO schedule2 VALUES (default, 'leothx_x','2023-01-20','모임','애들 모임');

SELECT * FROM schedule2 WHERE mid='leothx_x' and sDate = '2023-1' order by sDate;
SELECT * FROM schedule2 WHERE mid='leothx_x' and sDate = '2023-01' order by sDate;
SELECT * FROM schedule2 WHERE mid='leothx_x' and sDate like concat ('%','2023-01','%') order by sDate;
SELECT * FROM schedule2 WHERE mid='leothx_x' and date_format(sDate, '%Y-%m') = '2023-01' order by sDate;
SELECT * FROM schedule2 WHERE mid='leothx_x' and date_format(sDate, '%Y-%m') = '2023-01' group by substring(sDate,1,7) order by sDate;
SELECT sDate,part,count(*) FROM schedule2 WHERE mid='leothx_x' and date_format(sDate, '%Y-%m') = '2023-01' group by part, sDate order by sDate, part;

