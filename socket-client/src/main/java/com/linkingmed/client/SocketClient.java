package com.linkingmed.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.linkingmed.config.ServerConfig;

@ClientEndpoint
@Component
public class SocketClient {
	private static Logger logger = LoggerFactory.getLogger(SocketClient.class);
	private Session session;
	// 重连间隔时间
	private static final int reConnectTime = 10 * 1000;
	
	private static  boolean connectStatus = false;

	@OnOpen
	public void open(Session session) {
		logger.info("服务端连接建立成功");
		connectStatus=true;
		this.session = session;
	}

	@OnMessage
	public void onMessage(String message) throws Exception {
		logger.info("收到服务器端的消息: " + message);
		ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
		Dispatch sapo = sap.getObject();
		try {
			if("LM".equals(message)||"success".equals(message)){
				return;
			}
			// 音量 0-100
			sap.setProperty("Volume", new Variant(100));
			// 语音朗读速度 -10 到 +10
			sap.setProperty("Rate", new Variant(-2));
			Dispatch.call(sapo, "Speak", new Variant(message));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sapo.safeRelease();
			sap.safeRelease();
		}

	}

	@OnClose
	public static void onClose() {
		// 重连
		connectStatus=false;
		reConnect();
	}

	/**
	 * 发送客户端消息到服务端
	 * @param message
	 *  消息内容
	 */
	public void send(String message) {
		this.session.getAsyncRemote().sendText(message);
	}

	public static void connect() {
		ServerConfig config = ServerConfig.loadProperties();
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		SocketClient client = new SocketClient();
		try {
			container.connectToServer(client, new URI("ws://" + config.getUrl() + "/" + config.getRoomNum()));
		} catch (DeploymentException | IOException | URISyntaxException e) {
			logger.info("服务器连接失败,准备重新连接");
			e.printStackTrace();
			reConnect();
		}
		//heartbeat(client);
	}

	public static void reConnect() {
		long start = System.currentTimeMillis();
		while (!connectStatus) {
			long end = System.currentTimeMillis();
			if (end - start > reConnectTime) {
				logger.info("重连请求...");
				connect();
				start = end;
			}
		}
	}

	public static void heartbeat(SocketClient client) {
		// 心跳
		long start = System.currentTimeMillis();
		while (true) {
			long end = System.currentTimeMillis();
			if (end - start > reConnectTime) {
				logger.info("心跳.....LM");
				client.send("LM");
				start = end;
			}
		}
	}

}
