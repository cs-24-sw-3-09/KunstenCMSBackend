package com.github.cs_24_sw_3_09.CMS.socketConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.serviceImpl.DisplayDeviceServiceImpl;

import jakarta.annotation.PreDestroy;

@Component
public class SocketIOModule {
    private final SocketIOServer server;
    private DisplayDeviceService displayDeviceService;

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

    @Autowired
    @Lazy
    public void setDisplayDeviceService(DisplayDeviceService displayDeviceService) {
        this.displayDeviceService = displayDeviceService;
    }

    private ConnectListener onConnected() {
        return (client -> {
            int deviceId = Integer.parseInt(client.getHandshakeData().getSingleUrlParam("id"));
            client.joinRoom(String.valueOf(deviceId));
            displayDeviceService.connectScreen(deviceId);
            System.out.println("Device " + deviceId + " connected: " + client.getRemoteAddress());
        });
    }

    private DisconnectListener onDisconnected() {
        return (client -> {
            int deviceId = Integer.parseInt(client.getHandshakeData().getSingleUrlParam("id"));
            displayDeviceService.disconnectScreen(deviceId);
            System.out.println("Device disconnected: " + client.getRemoteAddress() + " id:" + deviceId);
        });
    }

    public void sendContent(int screenId, ContentEntity contentEntity) {
        BroadcastOperations roomOperations = server.getRoomOperations(String.valueOf(screenId));
        roomOperations.sendEvent("content", contentEntity);
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
