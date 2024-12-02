package com.github.cs_24_sw_3_09.CMS.socketConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.tasks.MonitorGracePeriodForDisplayDevices;

import jakarta.annotation.PreDestroy;

@Component
public class SocketIOModule {
    private final SocketIOServer server;

    @Autowired
    private MonitorGracePeriodForDisplayDevices monitorGracePeriodForDisplayDevices;

    // Constructor with parameters for hostname and port
    @Autowired
    public SocketIOModule(@Value("${socketio.hostname:0.0.0.0}") String hostname,
            @Value("${socketio.port:3051}") int port) {
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

            // Extract device ID (assume it's available as part of the client or context)
            int deviceId = Integer.parseInt(client.getHandshakeData().getSingleUrlParam("id"));
            monitorGracePeriodForDisplayDevices.sendDisconnectMailWithGrace(deviceId);
        });
    }

    public void sendContent(int screenId, ContentEntity contentEntity) {
        BroadcastOperations roomOperations = server.getRoomOperations(String.valueOf(screenId));
        roomOperations.sendEvent("content", contentEntity);
    }

    public boolean isConnected(int screenId) {
        return server.getRoomOperations(String.valueOf(screenId)).getClients().size() > 0;
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