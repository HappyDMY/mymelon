package kr.co.mymelon.media;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import net.utility.UploadSaveManager;
import net.utility.Utility;

@Controller
public class MediaCont {
	@Autowired	// �ڵ����� ������ ��ü�� ���ο��� // �����������̳ʿ� �̹� �����Ǿ� �����Ƿ� ������ new NediaGroupDAO ���� �ʾƵ� �ȴ�.
	private MediaDAO dao;
	
	
	public MediaCont() {
		System.out.println("--------MediaCont() ��ü ������");
	}

	@RequestMapping(value="/media/list.do", method=RequestMethod.GET)
	public ModelAndView list(MediaDTO dto) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("media/list");
		mav.addObject("root", Utility.getRoot());
		mav.addObject("mediagroupno", dto.getMediagroupno());	
		mav.addObject("list", dao.list(dto));	
		return mav;
	}//list end
	
	@RequestMapping(value="/media/create.do", method=RequestMethod.GET)
	public ModelAndView createForm(MediaDTO dto) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("media/createForm");
		mav.addObject("root", Utility.getRoot());
		mav.addObject("mediagroupno", dto.getMediagroupno());	
		return mav;
	}//creatForm end
	
	@RequestMapping(value="/media/create.do", method=RequestMethod.POST)
	public ModelAndView createProc(MediaDTO dto, HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("media/msgView");
		mav.addObject("root", Utility.getRoot());
//		------------------------------------
//		���۵� ���� ó��  	-> ���� ������ /media/storage ������ ����
//					-> ���� ������ /media/temp ������ ����
		String basePath = req.getRealPath("/media"); 
//serverFullPath: D:\Java1113\wsp\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\mymelon\media
		System.out.println("posterMF basePath = "+ basePath);		
		// 1) <input type='file' name='posterMF' size='50'>
		MultipartFile posterMF = dto.getPosterMF();
		String poster = UploadSaveManager.saveFileSpring30(posterMF, basePath+"/storage"); //���� �����ϰ� �����ӵ� ���ϸ� ��ȯ
		dto.setPoster(poster); // dto ��ü�� poster���ϸ� ���		
		
		// 2) <input type='file' name='filenameMF' size='50'>
		MultipartFile filenameMF = dto.getFilenameMF();
		String filename = UploadSaveManager.saveFileSpring30(filenameMF, basePath+"/temp"); //���� �����ϰ� �����ӵ� ���ϸ� ��ȯ
		dto.setFilename(filename); // dto ��ü�� poster���ϸ� ���		
		dto.setFilesize(filenameMF.getSize());
//		------------------------------------
		
		int cnt = dao.create(dto);
		if (cnt==0) {
			mav.addObject("msg1", "<p>���� ��� ����</p>");
			mav.addObject("img", "<img src='../images/fail.png'>");
			mav.addObject("link1", "<input type='button' value='�ٽýõ�' onclick='javascript:history.back()'>");
			mav.addObject("link2", "<input type='button' value='���' onclick='location.href=\"./list.do?mediagroupno="+dto.getMediagroupno()+"\"'>");
		}else {
			mav.addObject("msg1", "<p>���� ��� ����</p>");
			mav.addObject("img", "<img src='../images/sound.png'>");
			mav.addObject("link1", "<input type='button' value='�߰����' onclick='location.href=\"./create.do\"'>");
			mav.addObject("link2", "<input type='button' value='���' onclick='location.href=\"./list.do?mediagroupno="+dto.getMediagroupno()+"\"'>");
		}//if end
		return mav;
	}//createProc end
	
	
	@RequestMapping(value="/media/read.do", method=RequestMethod.GET)
	public ModelAndView read(int mediano) {
		ModelAndView mav = new ModelAndView();
		MediaDTO dto = new MediaDTO();
		dto = dao.read(mediano);
		ArrayList<MediaDTO> list = new ArrayList<MediaDTO>();
		list = dao.list(dto);
		if (dto!=null) {
			String filename = dto.getFilename();
			filename=filename.toLowerCase();
			if (filename.endsWith(".mp3")) {
				mav.setViewName("media/readMP3");
			} else if(filename.endsWith(".mp4")){
				mav.setViewName("media/readMP4");
			}			
			
		}//if (dto!=null) end
		
		mav.addObject("root", Utility.getRoot());
		mav.addObject("dto", dto);
		mav.addObject("list", list);
		System.out.println("root = "+Utility.getRoot());
		return mav;
	}//read end
	
	@RequestMapping(value="/media/delete.do", method=RequestMethod.GET)
	public ModelAndView deleteForm(MediaDTO dto) {
		ModelAndView mav = new ModelAndView();
	
		mav.setViewName("media/deleteForm");		
		mav.addObject("root", Utility.getRoot());
		mav.addObject("dto", dao.read(dto.getMediano()));
		return mav;
	}//creatForm end
	
	@RequestMapping(value="/media/delete.do", method=RequestMethod.POST)
	public ModelAndView deleteProc(MediaDTO dto, HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();	
		mav.setViewName("media/msgView");		
		mav.addObject("root", Utility.getRoot());
		MediaDTO oldDTO = dao.read(dto.getMediano());
		
		int cnt = dao.delete(dto.getMediano());
		if (cnt==0) {
			mav.addObject("msg1", "<p>���� ���� ����</p>");
			mav.addObject("img", "<img src='../images/fail.png'>");
			mav.addObject("link1", "<input type='button' value='�ٽýõ�' onclick='javascript:history.back()'>");
			mav.addObject("link2", "<input type='button' value='�׷���' onclick='location.href=\"./list.do\"'>");
		}else {
			String basepath=req.getRealPath("/media");			
			System.out.println(req.getPathInfo()); 
			UploadSaveManager.deleteFile(basepath+"/storage", oldDTO.getPoster());
			UploadSaveManager.deleteFile(basepath+"/temp", oldDTO.getFilename());
			mav.addObject("msg1", "<script type=\"text/javascript\">alert(\"���� �Ǿ����ϴ�.\");window.location.href='./list.do'</script>");
		}//if end
		
		return mav;
	}//deleteProc end
	
	@RequestMapping(value="/media/update.do", method=RequestMethod.GET)
	public ModelAndView updateForm(MediaDTO dto) {
		ModelAndView mav = new ModelAndView();
	
		mav.setViewName("media/updateForm");		
		mav.addObject("root", Utility.getRoot());
		mav.addObject("dto", dao.read(dto.getMediano()));
		return mav;
	}//updateForm end
	
	@RequestMapping(value="/media/update.do", method=RequestMethod.POST)
	public ModelAndView updateProc(MediaDTO dto, HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();	
		mav.setViewName("media/msgView");		
		mav.addObject("root", Utility.getRoot());
		MediaDTO oldDTO = dao.read(dto.getMediano());
//		------------------------------------
//		���۵� ���� ó��  	-> ���� ������ /media/storage ������ ����
//					-> ���� ������ /media/temp ������ ����
		String basePath = req.getRealPath("/media"); 
//serverFullPath: D:\Java1113\wsp\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\mymelon\media
		System.out.println("posterMF basePath = "+ basePath);		
		// 1) <input type='file' name='posterMF' size='50'>
		MultipartFile posterMF = dto.getPosterMF();
		String poster = UploadSaveManager.saveFileSpring30(posterMF, basePath+"/storage"); //���� �����ϰ� �����ӵ� ���ϸ� ��ȯ
		dto.setPoster(poster); // dto ��ü�� poster���ϸ� ���		
		
		// 2) <input type='file' name='filenameMF' size='50'>
		MultipartFile filenameMF = dto.getFilenameMF();
		String filename = UploadSaveManager.saveFileSpring30(filenameMF, basePath+"/temp"); //���� �����ϰ� �����ӵ� ���ϸ� ��ȯ
		dto.setFilename(filename); // dto ��ü�� poster���ϸ� ���		
		dto.setFilesize(filenameMF.getSize());
//		------------------------------------
		
		
		int cnt = dao.update(dto);
		if (cnt==0) {
			mav.addObject("msg1", "<p>���� ���� ����</p>");
			mav.addObject("img", "<img src='../images/fail.png'>");
			mav.addObject("link1", "<input type='button' value='�ٽýõ�' onclick='javascript:history.back()'>");
			mav.addObject("link2", "<input type='button' value='�׷���' onclick='location.href=\"./list.do\"'>");
		}else {
			String basepath=req.getRealPath("/media");			
			System.out.println(req.getPathInfo()); 
			UploadSaveManager.deleteFile(basepath+"/storage", oldDTO.getPoster());
			UploadSaveManager.deleteFile(basepath+"/temp", oldDTO.getFilename());
			mav.addObject("msg1", "<script type=\"text/javascript\">alert(\"���� �Ǿ����ϴ�.\");window.location.href='./list.do?mediagroupno="+dto.getMediagroupno()+"'</script>");
		}//if end
		
		return mav;
	}//updateProc end
}//class end
