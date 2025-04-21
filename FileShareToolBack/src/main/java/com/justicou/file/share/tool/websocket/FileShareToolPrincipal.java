package com.justicou.file.share.tool.websocket;

import com.justicou.file.share.tool.db.model.FileShareToolUser;

import java.security.Principal;

public class FileShareToolPrincipal implements Principal {
    private final FileShareToolUser user;

    public FileShareToolPrincipal(FileShareToolUser user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    public FileShareToolUser getUser() {
        return user;
    }
}
