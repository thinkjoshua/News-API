
package models;

public class Sitemap {
    private String describe;
    private String path;
    private String id;

    public Sitemap(String describe, String path) {
        this.describe = describe;
        this.path = path;
    }

    public String getDescribe() {
        return describe;
    }

    public String getPath() {
        return path;
    }

    public String getId() {
        return id;
    }
}