package jupar.utils;

import java.io.FileInputStream;
import java.security.MessageDigest;

public class JuparUtils {
	/**
	 * returns a lowercase SHA-1 hash to the input file
	 * @param file - the file to compute the SHA-1 for
	 * @return hash of the string
	 */
	@SuppressWarnings("resource")
	public static String getFileHash(String file) {
		try {
			
		    MessageDigest md = MessageDigest.getInstance("SHA1");
		    FileInputStream fis = new FileInputStream(file);
		    byte[] dataBytes = new byte[1024];
		 
		    int nread = 0; 
		 
		    while ((nread = fis.read(dataBytes)) != -1) {
		      md.update(dataBytes, 0, nread);
		    };
		 
		    byte[] mdbytes = md.digest();
		 
		    //convert the byte to hex format
		    StringBuffer sb = new StringBuffer("");
		    for (int i = 0; i < mdbytes.length; i++) {
		    	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		    }
		    return sb.toString().toLowerCase();
		    
		} catch (Exception e) {
		}
		return null;
	}
	
}
