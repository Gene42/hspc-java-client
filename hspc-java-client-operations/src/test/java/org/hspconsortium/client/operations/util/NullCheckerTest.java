package org.hspconsortium.client.operations.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class NullCheckerTest {

	@Test
	public void testIsNullishString(){
		String str= null;
		assertTrue(NullChecker.isNullish(str));
		assertTrue(NullChecker.isNullish(""));
		assertTrue(NullChecker.isNullish("    "));
		assertTrue(NullChecker.isNullish("  null  "));
		assertTrue(NullChecker.isNullish("    null"));
		assertFalse(NullChecker.isNullish("abcXYZ"));
	}
	
	@Test
	public void testIsNotNullishString(){
		String str= null;
		assertFalse(NullChecker.isNotNullish(str));
		assertFalse(NullChecker.isNotNullish(""));
		assertFalse(NullChecker.isNotNullish("    "));
		assertTrue(NullChecker.isNotNullish("abcXYZ"));
	}
	
	@Test
	public void testIsNullishForObject(){
		Object obj=null;
		assertTrue(NullChecker.isNullish(obj));
		assertFalse(NullChecker.isNullish(new Object()));
	}
	
	@Test
	public void testIsNotNullishForObject(){
		Object obj=null;
		assertFalse(NullChecker.isNotNullish(obj));
		assertTrue(NullChecker.isNotNullish(new Object()));
	}
	
	@Test
	public void testIsNullishForObjectArray(){
		Object[] obj=null;
		assertTrue(NullChecker.isNullish(obj));
		assertTrue(NullChecker.isNullish(new Object[0]));
		assertFalse(NullChecker.isNullish(new Object[1]));
	}
	
	@Test
	public void testIsNotNullishForObjectArray(){
		Object[] obj=null;
		assertFalse(NullChecker.isNotNullish(obj));
		assertFalse(NullChecker.isNotNullish(new Object[0]));
		assertTrue(NullChecker.isNotNullish(new Object[1]));
	}
}
