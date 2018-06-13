package org.hspconsortium.client.operations.util;

public class NullChecker {

	public static boolean isNullish(String str){
		return str==null || str.trim().equalsIgnoreCase("");
	}
	
	public static boolean isNotNullish(String str){
		return !isNullish(str);
	}
	
	public static boolean isNullish(Object obj){
		return obj==null;
	}
	
	public static boolean isNotNullish(Object obj){
		return !isNullish(obj);
	}
	
	public static boolean isNullish(Object[] obj){
		return obj==null || obj.length ==0;
	}
	
	public static boolean isNotNullish(Object[] obj){
		return !isNullish(obj);
	}
}
