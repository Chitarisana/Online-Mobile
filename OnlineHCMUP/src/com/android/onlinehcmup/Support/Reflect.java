package com.android.onlinehcmup.Support;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflect {
	public static void refectMethod(String methodName, String className)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, NoSuchMethodException,
			IllegalArgumentException, InvocationTargetException {
		Object[] obj = {};// for method1()
		// Object[] obj={"hello"}; for method1(String str)
		// Object[] obj={"hello",1}; for method1(String str,int number)
		// Step 2) Create a class array which will hold the signature of the
		// method being called.

		Class<?>[] params = new Class[obj.length];
		for (int i = 0; i < obj.length; i++) {
			if (obj[i] instanceof Integer) {
				params[i] = Integer.TYPE;
			} else if (obj[i] instanceof String) {
				params[i] = String.class;
			} else if (obj[i] instanceof Double) {
				params[i] = Double.TYPE;
			}
			// you can do additional checks for other data types if you want.
		}

		Class<?> cls = Class.forName(className);
		Object _instance = cls.newInstance();
		Method method = cls.getDeclaredMethod(methodName, params);
		method.invoke(_instance, obj);
	}

	public static Field[] reflectField(String className) {
		try {
			Class<?> cls = Class.forName(className);
			Field[] flds = cls.getFields();
			return flds;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}