package com.github.cs_24_sw_3_09.CMS.socketConnection;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import jakarta.annotation.PreDestroy;

public class SocketIOModule {
    private final SocketIOServer server;

    public SocketIOModule(String hostname, int port) {
        Configuration configuration = new Configuration();
        configuration.setHostname(hostname);
        configuration.setPort(port);
        SocketConfig socketConfiguration = new SocketConfig();
        socketConfiguration.setReuseAddress(true);
        configuration.setSocketConfig(socketConfiguration);
        this.server = new SocketIOServer(configuration);
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
    }

    private ConnectListener onConnected() {
        return (client -> {
            int deviceId = Integer.parseInt(client.getHandshakeData().getSingleUrlParam("id"));
            client.joinRoom(String.valueOf(deviceId));
            System.out.println("Device " + deviceId + " connected: " + client.getRemoteAddress());
        });
    }

    private DisconnectListener onDisconnected() {
        return (client -> {
            System.out.println("Device disconnected: " + client.getRemoteAddress());
        });
    }

    public void start() {
        server.start();
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            System.out.println("Stopping SocketIO server...");
            server.stop();
        }
    }
}
