package fx.excel.upload.util;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang.ArrayUtils;

public class ExcelUtil {
	
	private static int ascii_alpha_start = 65;
	
	private static int alpha_amount = 26;
	
	public static String createCelNumber(int index) {
		byte[] nums = null;
		
		if (index < 0) {
			throw new IllegalArgumentException();
		}
		
		for (int i = index;;) {
			int divide = (i / alpha_amount), mod = (i % alpha_amount);
			
			byte asciiValue = new Integer(ascii_alpha_start + mod).byteValue();
			nums = ArrayUtils.add(nums, asciiValue);
			
			if (i < alpha_amount) {
				break;
			}
			i = (divide - 1);
		}
		ArrayUtils.reverse(nums);
		return new String(nums, StandardCharsets.US_ASCII);
	}
}
