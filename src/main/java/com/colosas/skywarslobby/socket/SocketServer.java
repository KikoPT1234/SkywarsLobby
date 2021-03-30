package com.colosas.skywarslobby.socket;

import com.colosas.skywarslobby.SkywarsLobby;
import com.colosas.skywarslobby.objects.SkywarsServer;
import com.colosas.skywarslobby.socket.event.Event;
import org.apache.commons.lang.ArrayUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class SocketServer {

    private ServerSocket server;

    private final Set<Socket> clientServers = new HashSet<>();
    private final Map<Integer, DataInputStream> inputStreams = new HashMap<>();
    private final Map<Integer, DataOutputStream> outputStreams = new HashMap<>();
    private final Map<Integer, Thread> threads = new HashMap<>();

    private Thread mainThread;

    private Set<Event> events = new HashSet<>();

    public SocketServer(int port) {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainThread = new Thread(() -> {
            Socket tempSocket = null;
            while (!server.isClosed()) {
                try {
                    tempSocket = server.accept();

                    SkywarsLobby.getInstance().getLogger().info("New connection!");

                    DataInputStream in = new DataInputStream(new BufferedInputStream(tempSocket.getInputStream()));
                    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(tempSocket.getOutputStream()));

                    clientServers.add(tempSocket);
                    inputStreams.put(tempSocket.getPort(), in);
                    outputStreams.put(tempSocket.getPort(), out);
                } catch (IOException e) {
                    if (!server.isClosed()) e.printStackTrace();
                }

                if (tempSocket == null) return;

                final Socket socket = tempSocket;

                Thread thread = new Thread(() -> {
                    while (!socket.isClosed()) {
                        DataInputStream in = inputStreams.get(socket.getPort());
                        DataOutputStream out = outputStreams.get(socket.getPort());

                        try {
                            String message = in.readUTF();
                            String[] messageArray = message.split(";");

                            String name = messageArray[0];
                            String[] args = (String[]) ArrayUtils.remove(messageArray, 0);

                            if (name.equalsIgnoreCase("close")) {
                                inputStreams.remove(socket.getPort());
                                outputStreams.remove(socket.getPort());
                                in.close();
                                out.close();
                                clientServers.remove(socket);
                                socket.close();
                                break;
                            }

                            String[] response = null;

                            for (Event event : events) {
                                if (event.getEventName().equalsIgnoreCase(name)) {
                                    response = event.run(socket.getPort(), args);
                                    break;
                                }
                            }

                            if (response != null) {
                                sendMessage(socket.getPort(), name, response);
                            }
                        } catch (IOException e) {
                            if (!server.isClosed()) e.printStackTrace();
                        }
                    }
                });
                threads.put(socket.getPort(), thread);
                thread.start();
            }
        });

        mainThread.start();
    }

    public void sendMessage(int port, String name, String[] args) {
        Socket socket = null;

        for (Socket s : clientServers) {
            if (s.getPort() == port) {
                socket = s;
                break;
            }
        }

        if (socket == null) return;

        DataOutputStream out = outputStreams.get(port);
        try {
            out.writeUTF(name + ";" + String.join(";", args));
            out.flush();
        } catch (IOException e) {
            if (!server.isClosed()) e.printStackTrace();
        }
    }

    public void sendMessage(int port, String name) {
        Socket socket = null;

        for (Socket s : clientServers) {
            if (s.getPort() == port) {
                socket = s;
                break;
            }
        }

        if (socket == null) return;

        DataOutputStream out = outputStreams.get(port);
        try {
            out.writeUTF(name);
            out.flush();
        } catch (IOException e) {
            if (!server.isClosed()) e.printStackTrace();
        }
    }

    public void broadcast(String name, String[] args) {
        for (Socket socket : clientServers) {
            DataOutputStream out = outputStreams.get(socket.getPort());
            try {
                out.writeUTF(name + ";" + String.join(";", args));
                out.flush();
            } catch (IOException e) {
                if (!server.isClosed()) e.printStackTrace();
            }
        }
    }

    public void broadcast(String name) {
        for (Socket socket : clientServers) {
            DataOutputStream out = outputStreams.get(socket.getPort());
            try {
                out.writeUTF(name);
                out.flush();
            } catch (IOException e) {
                if (!server.isClosed()) e.printStackTrace();
            }
        }
    }

    public void registerEvent(Event event) {
        events.add(event);
    }

    public void close() {
        broadcast("close");
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadStop() {
        broadcast("reload");
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
