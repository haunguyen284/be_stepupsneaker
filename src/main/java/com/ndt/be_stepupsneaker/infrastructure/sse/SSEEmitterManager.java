package com.ndt.be_stepupsneaker.infrastructure.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SSEEmitterManager {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
    }

    public void removeEmitter(SseEmitter emitter) {
        emitters.remove(emitter);
    }

    public void sendMessage(Object object) {
        emitters.forEach(emitter -> {
            try {
                emitter.send(object);
            } catch (IOException e) {
                emitter.complete();
                removeEmitter(emitter);
            }
        });
    }
}
