--mediagroup.sql
-- �̵�� �׷� ���̺�
create table mediagroup(
	mediagroupno	number			not null primary key, --�׷��
	title 			varchar(255)	not null --�׷�����
);

--���߰� 
�׸���ȣ : �׸���ȣ �ִ밪+1
�׷����� : '2018�� ������';

INSERT INTO MEDIAGROUP(mediagroupno, title)
VALUES((SELECT NVL(max(mediagroupno),0)+1 FROM mediagroup), '2018�� ������');

--��� 
select mediagroupno, title
from mediagroup
order by mediagroupno desc;

DROP TABLE mediagroup;