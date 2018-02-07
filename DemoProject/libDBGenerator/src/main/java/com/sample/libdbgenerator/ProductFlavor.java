package com.sample.libdbgenerator;

/**
 * Created by Edward on 9/12/2017.
 */

public class ProductFlavor {
    private String name;
    private String datacache;
    private String area;
    private String retro;
    private String clientVersion;
    private boolean isStage;

    public ProductFlavor(String appName, String datacache, String area,
                         String retro, String clientVersion, boolean isStage) {
        this.name = appName;
        this.datacache = datacache;
        this.area = area;
        this.retro = retro;
        this.clientVersion = clientVersion;
        this.isStage = isStage;
    }

    public String getName() {
        return name;
    }

    public String getDatacache() {
        return datacache;
    }

    public String getArea() {
        return area;
    }

    public String getRetro() {
        return retro;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public boolean isStage() {
        return isStage;
    }
}
