package kr.co.mymelon.media;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.mymelon.mediagroup.MediagroupDTO;
import net.utility.DBClose;
import net.utility.DBOpen;

@Component
public class MediaDAO {
	@Autowired // Conmponent annotaion ���� ���� ��ü�� ����Ϸ��� ��ü�� ���� ������ �Ǿ���Ѵ�.
	private DBOpen dbopen;
	@Autowired
	private DBClose dbclose;
	
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	StringBuffer sql = null;
	ArrayList<MediaDTO> list = null;
	
	public ArrayList<MediaDTO> list(MediaDTO dto) {
		try {
			System.out.println("dto.getMediagroupno()======="+dto.getMediagroupno());
			con =dbopen.getConnection();
			sql= new StringBuffer();
			sql.append(" SELECT mediano,title,poster,filename,mediagroupno,rdate,filesize ");
			sql.append(" FROM MEDIA WHERE MVIEW='Y' AND MEDIAGROUPNO= ? ORDER BY MEDIANO ");
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, dto.getMediagroupno());
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				list = new ArrayList<MediaDTO>();
				do {
					dto = new MediaDTO();
					dto.setTitle(rs.getString("title"));
					dto.setMediano(rs.getInt("mediano"));
					dto.setPoster(rs.getString("poster"));
					dto.setFilename(rs.getString("filename"));
					dto.setMediagroupno(rs.getInt("mediagroupno"));
					dto.setRdate(rs.getString("rdate"));
					dto.setFilesize(rs.getLong("filesize"));
					list.add(dto);	
				} while (rs.next());
				System.out.println("list.isEmpty()->"+list.isEmpty());
			} else {
				list.clear();
				System.out.println("�ش� ������ ����");
			}
			
		} catch (Exception e) {
			System.out.println("��Ϻ��� ���� : "+e);
		} finally {
			dbclose.close(con, pstmt, rs);
		}
		
		return list;
		
	}//list end

	public int create(MediaDTO dto) {
		int res = 0;
		try {
			con =dbopen.getConnection();
			sql= new StringBuffer();
			sql.append(" INSERT INTO MEDIA(mediano, title,poster,filename,filesize,mview,rdate,mediagroupno) ");
			sql.append(" VALUES((SELECT NVL(max(mediano),0)+1 FROM media), ?, ?, ?, ?, 'Y', SYSDATE, ?) ");
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getPoster());
			pstmt.setString(3, dto.getFilename());
			pstmt.setLong(4, dto.getFilesize());
			pstmt.setInt(5, dto.getMediagroupno());
			res = pstmt.executeUpdate();
			
			System.out.println("�Է°��:"+res);
			
		} catch (Exception e) {
			System.out.println("�߰� ����"+e);
		} finally {
			dbclose.close(con, pstmt, rs);
		}
		
		return res;
	}

	public MediaDTO read(int mediano) {
		MediaDTO dto = new MediaDTO();
		System.out.println("mediano="+mediano);
		try {
			con =dbopen.getConnection();
			sql= new StringBuffer();
			sql.append(" SELECT mediano,title,poster,filename,mediagroupno,rdate ");
			sql.append(" FROM MEDIA WHERE MVIEW='Y' AND MEDIANO= ? ");
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, mediano);
			rs = pstmt.executeQuery();
			if (rs.next()) {			
				dto.setTitle(rs.getString("title"));
				dto.setMediano(rs.getInt("mediano"));
				dto.setPoster(rs.getString("poster"));
				dto.setFilename(rs.getString("filename"));
				dto.setMediagroupno(rs.getInt("mediagroupno"));
				dto.setRdate(rs.getString("rdate"));				
//				System.out.println("list.isEmpty()->"+list.isEmpty());
			} else {
				list.clear();
				System.out.println("�ش� ������ ����");
			}
			
		} catch (Exception e) {
			System.out.println("���� ��ȸ ���� : "+e);
		} finally {
			dbclose.close(con, pstmt, rs);
		}
		
		return dto;
	}

	public int delete(int mediano) {
		int res = 0;
		try {
			con =dbopen.getConnection();
			sql= new StringBuffer();
			sql.append(" DELETE FROM MEDIA WHERE MEDIANO = ? ");
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, mediano);
			res = pstmt.executeUpdate();
			
			System.out.println("��� res:"+res);
			
		} catch (Exception e) {
			System.out.println("���� ���� : "+e);
		} finally {
			dbclose.close(con, pstmt, rs);
		}
		
		return res;
	}//delete end

	public int update(MediaDTO dto) {
		int res = 0;
		try {
			con =dbopen.getConnection();
			sql= new StringBuffer();
			sql.append(" UPDATE MEDIA ");
			sql.append(" SET title=?,poster=?,filename=?,filesize=?,rdate=sysdate WHERE mediano=? ");
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getPoster());
			pstmt.setString(3, dto.getFilename());
			pstmt.setLong(4, dto.getFilesize());
			pstmt.setInt(5, dto.getMediano());
			res = pstmt.executeUpdate();
			
			System.out.println("��� res:"+res);
			
		} catch (Exception e) {
			System.out.println("���� ���� : "+e);
		} finally {
			dbclose.close(con, pstmt, rs);
		}
		
		return res;
	}//update end
	
}// class end
