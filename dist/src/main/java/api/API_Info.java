package api;

public class API_Info {

    private String api_version;
    private String api_point;
    private String api_docs;

    public API_Info(String api_version, String api_point, String api_docs) {
        this.api_version = api_version;
        this.api_point = api_point;
        this.api_docs = api_docs;
    }

    public String getApi_version() {return api_version; }
    public void setApi_version(String api_version) {this.api_version = api_version;}
    public String getApi_point() {return api_point; }
    public void setApi_point(String api_point) {this.api_version = api_point;}
    public String getApi_docs() {return api_docs; }
    public void setApi_docs(String api_docs) {this.api_docs = api_docs; }
}