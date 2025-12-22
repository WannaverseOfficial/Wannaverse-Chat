package com.wannaverse.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ServerPortProvider {

    private final WebServerApplicationContext ctx;

    @Autowired
    public ServerPortProvider(WebServerApplicationContext ctx) {
        this.ctx = ctx;
    }

    public int getPort() {
        return Objects.requireNonNull(ctx.getWebServer()).getPort();
    }
}
