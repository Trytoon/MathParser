package data;

import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import arithmetic.Function;

public class Register implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	static HashMap<String, Function> register;
	
	public Register() {
		register = new HashMap<String, Function>();
	}
	
	public static void addToRegister(Function fun) {
		if (fun != null) {
			String name = fun.getName();
			register.put(name, fun);
		}
	}
	
	public static HashMap<String, Function> getUniqueRegister() {
		return register;
	}
	
	public String toString() {
		String val = "";
		for (Map.Entry<String, Function> entry : register.entrySet()) {
		    String key = entry.getKey();
		    Function value = entry.getValue();
		    
		    val += key.toString() + ": " +value.toString() + '\n';
		}
		return val;
	}
	
	public static boolean functionExists(Function f) {
		
		if (f.equals(null)) {
			return false;
		} 
		
		String name = f.getName();
		return register.containsKey(name);
	}
	
	public int functionCount() {
		int i = 0;
		for (String s: register.keySet()) {
			i++;
		}
		return i;
	}
	
	public static boolean isRegularFunction(String functionName) {
		Class<?> c = null;
		try {
			c = Class.forName("java.lang.Math");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Method method = c.getDeclaredMethod(functionName, double.class);
		} catch (NoSuchMethodException e) {
			return false;
		} catch (SecurityException e) {
			return false;
		}
		
		return true;
	}
	
	public void writeObject(OutputStreamWriter oos) {
		
	}
}
