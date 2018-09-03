package com.linkingmed;

import java.net.URI;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

import com.linkingmed.client.SocketClient;
import com.linkingmed.config.ServerConfig;

@SpringBootApplication
public class Application implements EmbeddedServletContainerCustomizer{

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			SocketClient client = new SocketClient();
			ServerConfig config = ServerConfig.loadProperties();
			container.connectToServer(client, new URI("ws://" + config.getUrl()));
			client.send(config.getRoomNum());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        configurableEmbeddedServletContainer.setPort(55008);
    }
	
}
