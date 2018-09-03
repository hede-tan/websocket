package com.linkingmed.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerConfig {
	private int port;
	private String url;
	private String roomNum;
	
	public ServerConfig() {
		super();
	}

	public ServerConfig(int port, String url, String roomNum) {
		super();
		this.port = port;
		this.url = url;
		this.roomNum = roomNum;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@SuppressWarnings("finally")
	public static ServerConfig loadProperties(){
		Properties properties = new Properties();
        File exeWorkFile = new File("");//获取jar或exe或项目工作目录的路径
        String iniPath = exeWorkFile.getAbsolutePath()+File.separator+"serverInfo.ini";//获取.ini格式文件的路径
        ServerConfig serverInfo = new ServerConfig();
        try {
            properties.load(new FileInputStream(iniPath));//读取.ini格式文件中的信息
            //通过key获取.ini文件中对应的value值，并将该value值存储到一个对象中去
            serverInfo.setUrl(properties.getProperty("url"));
            serverInfo.setRoomNum(properties.getProperty("roomNum"));
            serverInfo.setPort(Integer.valueOf(properties.getProperty("port")));
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            return serverInfo;//将存有.ini格式文件中信息的对象返回
        }
	} 
	
}
