package com.linkingmed.api;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkingmed.server.SocketServer;

@RestController
public class SocketController {
	
	@GetMapping("/call")
	public boolean getUserBaseInfo(String msg,String port) {
		CopyOnWriteArraySet<SocketServer> webSocketSet = SocketServer.webSocketSet;
		try {
			for (SocketServer socketServer : webSocketSet) {
				socketServer.sendMessage(msg,port);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
