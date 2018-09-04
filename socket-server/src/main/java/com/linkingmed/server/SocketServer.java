package com.linkingmed.server;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/riac/{roomNum}")
@Component
public class SocketServer {
	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;

	// concurrent包的线程安全Set，用来存放每个客户端对应的socket对象。
	public static CopyOnWriteArraySet<SocketServer> webSocketSet = new CopyOnWriteArraySet<SocketServer>();
	//public static CopyOnWriteArraySet<SocketServer> webSocketSet = new CopyOnWriteArraySet<SocketServer>();
	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	
	private String roomNum;

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(@PathParam("roomNum")String roomNum,Session session) {
		this.session = session;
		this.roomNum = roomNum;
		webSocketSet.add(this); // 加入set中
		addOnlineCount(); // 在线数加1
		System.err.println("有新连接加入！当前在线人数为" + getOnlineCount());
		try {
			sendMessage("success");
		} catch (IOException e) {
			System.out.println("IO异常");
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		webSocketSet.remove(this); // 从set中删除
		subOnlineCount(); // 在线数减1
		System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message
	 *            客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String port, Session session) {
		System.out.println("客户端的端口:" + port);
		//收到消息后创建绑定session
		try {
			sendMessage(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 发生错误时调用
	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("发生错误");
		error.printStackTrace();
	}

	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}
	public void sendMessage(String message,String port) throws IOException {
		for (SocketServer item : webSocketSet) {
			if(item.getRoomNum().equals(port)){
				item.getSession().getBasicRemote().sendText(message);
			}
		}
	}

	/**
	 * 群发自定义消息
	 */
	public static void sendInfo(String message) throws IOException {
		for (SocketServer item : webSocketSet) {
			try {
				item.sendMessage(message);
			} catch (IOException e) {
				continue;
			}
		}
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		SocketServer.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		SocketServer.onlineCount--;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}
	
}
