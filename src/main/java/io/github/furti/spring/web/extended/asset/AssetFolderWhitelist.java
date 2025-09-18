package io.github.furti.spring.web.extended.asset;

import java.util.List;

public class AssetFolderWhitelist {

    private final List<String> allowedFolders;

    public AssetFolderWhitelist(List<String> allowedFolders) {
        super();
        this.allowedFolders = allowedFolders;
    }

    public boolean contains(String path) {
        return allowedFolders.contains(path);
    }
}
