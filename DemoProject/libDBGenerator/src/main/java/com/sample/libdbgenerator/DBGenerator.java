package com.sample.libdbgenerator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DBGenerator {

    static String argPath;
    static File[] productFlavors;
    static int productPosition;

    public static void main(String... args) {
        argPath = args[0];
        //get project dir path.
        String productFlavorPath = argPath + File.separator + "productFlavorConfigs";
        //ensure it exists/
        File productFlavorDirectory = assertDirectoryExists(productFlavorPath);

        //get all configs
        productFlavors = productFlavorDirectory.listFiles(
                (File dir, String name) -> name.contains(".json"));
        if (productFlavors != null && productFlavors.length > 0) {
            productPosition = 0;

            DBGenerator ttdbGenerator = new DBGenerator();
            ttdbGenerator.buildProductFlavor();
            System.out.println("Put a breakpoint on me please!");
        }
    }

    public void buildProductFlavor() {
        if (productPosition >= 0 && productPosition < productFlavors.length) {
            //parse
            ProductFlavor pf = loadAppConfig(productFlavors[productPosition]);
            // 1. Parse {}.json file for:
            //      a. datacache
            //      b. cityAbbrev
            if (pf != null) {
                System.out.println("Begin Creating database");
            } else {
                throw new IllegalArgumentException("ProductFlavor not found in appConfig");
            }
        } else {
            postMessage("Database caches created.");
        }
    }

    private static File assertDirectoryExists(String path) {
        File file = new File(path);
        assert file.isDirectory();
        assert file.isAbsolute();
        return file;
    }

    private static ProductFlavor loadAppConfig(File file) {
        ProductFlavor flavor = null;
        try (InputStream is = new FileInputStream(file)) {
            JsonParser jsonParser = new JsonParser();
            flavor = configureLoad((JsonObject) jsonParser.parse(new InputStreamReader(is, "UTF-8")));
        } catch (IOException e) {
            //
        }

        return flavor;
    }

    private static ProductFlavor configureLoad(JsonObject json) {
        //get our config
        JsonObject app = json.getAsJsonObject("app");

        //set the single item values.
        String appName = app.get("appName").getAsString();
        String datacache = app.get("datacache").getAsString();
        String area = app.get("area").getAsString();
        String retro = app.get("retro").getAsString();
        String clientVersion = app.get("clientVersion").getAsString();
        boolean isStage = Boolean.parseBoolean(app.get("isStage").getAsString());

        return new ProductFlavor(appName, datacache, area, retro, clientVersion, isStage);
    }

    public static void postMessage(String message) {
        System.out.println(message);
    }
}