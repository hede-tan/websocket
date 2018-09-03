package com.linkingmed.client;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

@ClientEndpoint
@Component
public class SocketClient {
	private static Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private Session session;
    @OnOpen
    public void open(Session session){
        logger.info("Client WebSocket is opening...");
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message){
    	// TODO Auto-generated method stub
	    ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
		  
	    Dispatch sapo = sap.getObject();
	    try {
	        // 音量 0-100
	        sap.setProperty("Volume", new Variant(100));
	        // 语音朗读速度 -10 到 +10
	        sap.setProperty("Rate", new Variant(-2));
	        logger.info("收到服务器端的消息: " + message);
	        logger.info("session"+session.getId());
	        Dispatch.call(sapo, "Speak", new Variant(message));
 
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        sapo.safeRelease();
	        sap.safeRelease();
	    }
    	
    }

    @OnClose
    public void onClose(){
        logger.info("Websocket closed");
    }

    /**
     * 发送客户端消息到服务端
     * @param message 消息内容
     */
    public void  send(String message){
        this.session.getAsyncRemote().sendText(message);
    }
	


}
