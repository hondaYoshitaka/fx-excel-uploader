package fx.excel.upload.util;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang.ArrayUtils;


/**
 * @author hondayoshitaka
 *
 */
public class ExcelUtil {
	
	private static int ascii_alpha_start = 65;
	
	private static int alpha_amount = 26;
	
	public static String createCelNumber(int index){
		if(index < 0){
			throw new IllegalArgumentException();
		}
		byte[] nums = null;
		for(int i = index;;){
			int divide = i / alpha_amount;
			int mod = i % alpha_amount;
			
			nums = ArrayUtils.add(nums, new Integer(ascii_alpha_start + mod).byteValue());
			if(i < alpha_amount){
				break;
			}
			i = divide;
		}
		return new String(nums,StandardCharsets.US_ASCII);
	}
}
