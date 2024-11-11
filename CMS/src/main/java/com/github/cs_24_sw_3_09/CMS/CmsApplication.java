package com.github.cs_24_sw_3_09.CMS;

//import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.cs_24_sw_3_09.CMS.socketConnection.SocketIOModule;

@SpringBootApplication
public class CmsApplication {

	private final SocketIOModule module;

	public CmsApplication() {
		module = new SocketIOModule("0.0.0.0", 3051);
		module.start(); // Start socket.io server
	}

	@Bean
	public SocketIOModule getModule() {
		return module;
	}
}
