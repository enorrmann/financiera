package ar.com.norrmann.financiera.web.util;

import javax.mail.internet.InternetAddress;

public class MailValidator {

	public static boolean isValidEmailAddress(String aEmailAddress) {
		if (aEmailAddress == null)return false;
		if (aEmailAddress.trim().isEmpty())return false;
		
		try {
			new InternetAddress(aEmailAddress);
			if (!hasNameAndDomain(aEmailAddress)) {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	private static boolean hasNameAndDomain(String aEmailAddress){
		    String[] tokens = aEmailAddress.split("@");
		    return 
		     tokens.length == 2 &&  !tokens[0].trim().isEmpty() && !tokens[1].trim().isEmpty()  ;
		  }

}
