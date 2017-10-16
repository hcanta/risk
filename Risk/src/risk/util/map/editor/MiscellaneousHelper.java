package risk.util.map.editor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;


public class MiscellaneousHelper {
	
	public static final String DATE_FORMAT_DEFAULT = "yyyyMMddHHmmssSSS";
	/**
	 * 
	 * Return true when string value is double
	 * 
	 * @param new_value
	 *            a String
	 * @return boolean
	 */
	public boolean isDouble(String new_value) {
		try {
			Double.parseDouble(new_value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * this method removes character from start to left
	 * 
	 * @param new_inputString
	 *            input string
	 * @param new_characters
	 *            characters to be removed
	 * @return string the result
	 */
	public String RemoveCharacterFromStrartorLeft(String new_inputString, String new_characters) {
		return new_inputString.replaceAll("^" + new_characters + "+", "");
		// return InputString.replaceAll("^0+(?!$)", "");
	}

	/**
	 * the method removes character from end or right
	 * 
	 * @param new_inputString
	 *            input string
	 * @param new_characters
	 *            characters to be removed
	 * @return string the result
	 */
	public String RemoveCharacterFromEndorRight(String new_inputString, String new_characters) {
		return new_inputString.replaceAll("\\" + new_characters + "+$", "");
	}

	/**
	 * this method removes character from both end
	 * 
	 * @param new_inputString
	 *            input string
	 * @param new_characters
	 *            characters to be removed
	 * @return string the result
	 */
	public String RemoveCharacterFromBothEnd(String new_inputString, String new_characters) {
		return new_inputString.replaceAll("^\\" + new_characters + "+|\\" + new_characters + "+$", "");
	}

	/**
	 * this method encodes the input to base64
	 * 
	 * @param new_input
	 *            string
	 * @return returns encoded string
	 */
	public String EncodeBase64(String new_input) {
		byte[] bytesEncoded = Base64.encodeBase64(new_input.getBytes());
		return new String(bytesEncoded);

	}

	/**
	 * this method decodes the string from base64 encoding
	 * 
	 * @param new_input
	 *            string base64
	 * @return string the result
	 */
	public String DecodeBase64(String new_input) {
		// Decrypt data on other side, by processing encoded data
		byte[] valueDecoded = Base64.decodeBase64(new_input.getBytes());
		return new String(valueDecoded);

	}

	/**
	 * This method return date in string form in Format 'yyyyMMddHHmmssSSS'
	 * 
	 * @return string date
	 */
	public String getCurrentDateStr() {
		return new SimpleDateFormat(DATE_FORMAT_DEFAULT).format(new Date());
	}

	/**
	 * This method return date in string form. The date format is depend upon
	 * the input Provied by user.
	 * 
	 * @param new_format new format
	 * @return date 
	 */
	public String getCurrentDateStr(String new_format) {
		return new SimpleDateFormat(new_format).format(new Date());
	}

	/**
	 * This method reads a file
	 * 
	 * @param new_file
	 *            file to read
	 * @return the data in the file
	 */
	public String readFile(File new_file) {
		try {
			return new String(Files.readAllBytes(Paths.get(new_file.getPath())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
