package com.github.cs_24_sw_3_09.CMS.socketConnection;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
    public SocketIOModule(String hostname, int port, String socketOrigin) {
        Configuration configuration = new Configuration();
        configuration.setHostname(hostname);
        configuration.setPort(port);
        configuration.setOrigin(socketOrigin);
        SocketConfig socketConfiguration = new SocketConfig();
        socketConfiguration.setReuseAddress(true);
        configuration.setSocketConfig(socketConfiguration);
        this.server = new SocketIOServer(configuration);

        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("changeContent", ScreenStatusMessage.class, new DataListener<ScreenStatusMessage>() {

            @Override
            public void onData(SocketIOClient client, ScreenStatusMessage data, AckRequest ackSender) throws Exception {
                BroadcastOperations broadcastOperations = server.getNamespace("/dashboard").getBroadcastOperations();
                broadcastOperations.sendEvent("changeContent", data);
            }
            
        });
    }

    private ConnectListener onConnected() {
        return (client -> {
            int deviceId = Integer.parseInt(client.getHandshakeData().getSingleUrlParam("id"));
            client.joinRoom(String.valueOf(deviceId));
        });
    }

    private DisconnectListener onDisconnected() {
        return (client -> {

            // Extract device ID (assume it's available as part of the client or context)
            int deviceId = Integer.parseInt(client.getHandshakeData().getSingleUrlParam("id"));
            monitorGracePeriodForDisplayDevices.sendDisconnectMailWithGrace(deviceId);
        });
    }

    public void sendContent(int screenId, ContentEntity contentEntity) {
        BroadcastOperations roomOperations = server.getRoomOperations(String.valueOf(screenId));
        try {
            String contentJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(contentEntity);
            roomOperations.sendEvent("content", contentJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
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