package net.utility;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MyAuthenticator extends Authenticator {
	// ����ϰ��� �ϴ� ���� �������� �������� ����/���
	private PasswordAuthentication pa;

	public MyAuthenticator() {
		pa = new PasswordAuthentication("soldesk@pretyimo.cafe24.com", "soldesk6901");
	}
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		//return super.getPasswordAuthentication();
		return pa;
	}
	
}// class end
