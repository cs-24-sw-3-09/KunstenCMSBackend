package com.github.cs_24_sw_3_09.CMS.socketConnection;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import java.util.Collection;
import java.util.List;

public class SocketIOModule {
    private final SocketIOServer server;

    public SocketIOModule(String hostname, int port) {
        Configuration configuration = new Configuration();
        configuration.setHostname(hostname);
        configuration.setPort(port);
        this.server = new SocketIOServer(configuration);
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("sendMessage", SocketIOMessage.class, this.receiveMessage());
    }

    private DataListener<SocketIOMessage> receiveMessage() {
        return ((client, data, ackSender) -> {
            System.out.println("Got message: " + data.getMessage());
        });
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

    private void broadcastMessage(String event, String message) {
        server.getBroadcastOperations().sendEvent(event, message);
    }

    public Collection<SocketIOClient> getClients() {
        return server.getAllClients();
    }

    public void clearDisplay(String uuid) {
        getClients().stream().filter(client -> client.getSessionId().toString().equals(uuid)).findAny()
                .ifPresent(client -> client.sendEvent("clear"));
    }

    public void startCarousel(String uuid, List<String> images) {
        getClients().stream().filter(client -> client.getSessionId().toString().equals(uuid)).findAny()
                .ifPresent(client -> client.sendEvent("carousel-images", images));
    }

    public void setImage(String uuid, String image) {
        getClients().stream().filter(client -> client.getSessionId().toString().equals(uuid)).findAny()
                .ifPresent(client -> client.sendEvent("set-image", image));
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop();
    }
}
