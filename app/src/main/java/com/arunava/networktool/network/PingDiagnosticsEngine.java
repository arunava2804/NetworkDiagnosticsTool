package com.arunava.networktool.network;

import com.arunava.networktool.model.PingResult;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PingDiagnosticsEngine {

    public interface PingCallback {
        void onComplete(List<PingResult> results);
    }

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public void runDiagnostics(PingCallback callback) {
        executor.execute(() -> {
            String[][] targets = {
                {"Google DNS", "8.8.8.8", "53"},
                {"Cloudflare DNS", "1.1.1.1", "53"},
                {"OpenDNS", "208.67.222.222", "53"},
                {"Google Web", "www.google.com", "80"}
            };

            List<PingResult> results = new ArrayList<>();
            for (String[] target : targets) {
                String label = target[0];
                String host = target[1];
                int port = Integer.parseInt(target[2]);

                long startTime = System.currentTimeMillis();
                boolean reachable = false;
                String resolvedIp = host;

                try {
                    InetAddress inet = InetAddress.getByName(host);
                    resolvedIp = inet.getHostAddress();
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(inet, port), 2000);
                    socket.close();
                    reachable = true;
                } catch (IOException ignored) {}

                long latency = System.currentTimeMillis() - startTime;
                results.add(new PingResult(label, resolvedIp, reachable ? latency : -1, reachable));
            }

            callback.onComplete(results);
        });
    }
}
