package com.linkingmed.api;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkingmed.server.SocketServer;

@RestController
public class SocketController {
	
	@Autowired
	private SocketServer socket;
	
	@GetMapping("/call")
	public boolean getUserBaseInfo(String msg,String port) {
		CopyOnWriteArraySet<SocketServer> webSocketSet = SocketServer.webSocketSet;
		try {
			socket.sendMessage(msg,port);
//			for (SocketServer socketServer : webSocketSet) {
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
