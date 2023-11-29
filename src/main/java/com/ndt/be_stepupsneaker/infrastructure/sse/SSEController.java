package com.ndt.be_stepupsneaker.infrastructure.sse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
public class SSEController {
    @Autowired
    private final SSEEmitterManager sseEmitterManager;

    public SSEController(SSEEmitterManager sseEmitterManager) {
        this.sseEmitterManager = sseEmitterManager;
    }

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect() {
        SseEmitter emitter = new SseEmitter();
        sseEmitterManager.addEmitter(emitter);
        emitter.onCompletion(() -> sseEmitterManager.removeEmitter(emitter));
        return emitter;
    }

}
