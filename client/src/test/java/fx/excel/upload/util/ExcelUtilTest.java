package fx.excel.upload.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ExcelUtilTest {
	
	@Test
	public void indexの0番目はAになる() {
		int index = 0;
		
		assertThat(ExcelUtil.createCelNumber(index), is("A"));
	}
	
	@Test
	public void indexの1番目はBになる() {
		int index = 1;
		
		assertThat(ExcelUtil.createCelNumber(index), is("B"));
	}
	
	@Test
	public void indexの24番目はYになる() {
		int index = 24;
		
		assertThat(ExcelUtil.createCelNumber(index), is("Y"));
	}
	
	@Test
	public void indexの25番目はZになる() {
		int index = 25;
		
		assertThat(ExcelUtil.createCelNumber(index), is("Z"));
	}
	
	@Test
	public void indexの26番目はAAになる() {
		int index = 26;
		
		assertThat(ExcelUtil.createCelNumber(index), is("AA"));
	}
	
	@Test
	public void indexの27番目はABになる() {
		int index = 27;
		
		assertThat(ExcelUtil.createCelNumber(index), is("AB"));
	}
	
	@Test
	public void indexの51番目はAZになる() {
		int index = 51;
		
		assertThat(ExcelUtil.createCelNumber(index), is("AZ"));
	}
	
	@Test
	public void indexの52番目はBAになる() {
		int index = 52;
		
		assertThat(ExcelUtil.createCelNumber(index), is("BA"));
	}
	
	@Test
	public void indexの702番目はAAAになる() {
		int index = 702;
		
		assertThat(ExcelUtil.createCelNumber(index), is("AAA"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void indexの0番目より小さい場合は例外になる() {
		int index = -1;
		
		ExcelUtil.createCelNumber(index);
		fail();
	}
}
