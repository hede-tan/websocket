package com.linkingmed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

import com.linkingmed.client.SocketClient;
import com.linkingmed.config.ServerConfig;

@SpringBootApplication
public class Application implements EmbeddedServletContainerCustomizer{
	
	private static int port=8080;
	
	public static void main(String[] args) {
		ServerConfig config = ServerConfig.loadProperties();
		if(config.getPort()>0){
			port=config.getPort();
		}
		SpringApplication.run(Application.class, args);
		SocketClient.connect();
		
	}
	
	@Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        configurableEmbeddedServletContainer.setPort(port);
    }
	
}
