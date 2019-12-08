package com.kevin.tankwar;

import java.io.IOException;
import java.util.Properties;

public class PropertyMgr {
	//static静态区：执行一次放置内存区，只调用一次。（单例模式）
	static Properties pros = new Properties();
	static {
		try {
			pros.load(PropertyMgr.class.getClass().getClassLoader().getSystemResourceAsStream("config/tank.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	public static String getProperty(String key) {
		
		return pros.getProperty(key);
	}
}
