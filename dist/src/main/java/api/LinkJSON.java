package api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;

public class LinkJSON extends JSON{

    private String shortUrl;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String longUrl;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String pageTitle;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String status;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
    private long clicks;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private List<LinkStatistics> analytics;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
    private long expireTime;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
    private long expireClicks;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
    private long createdTime;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public Integer code = null;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String message;

    public LinkJSON(){}

    public LinkJSON(String shortUrl, String longUrl, Integer code, String message) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.code = code;
        this.message = message;
    }

    public LinkJSON(String shortUrl, String longUrl, String pageTitle, String status, long clicks, List<LinkStatistics> analytics, long expireTime, long expireClicks, long createdTime, Integer code, String message) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.pageTitle = pageTitle;
        this.status = status;
        this.clicks = clicks;
        this.analytics = analytics;
        this.expireTime = expireTime;
        this.expireClicks = expireClicks;
        this.createdTime = createdTime;
        this.code = code;
        this.message = message;
    }


    public String getLongUrl() {
        return longUrl;
    }
    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }
    public String getShortUrl() {
        return shortUrl;
    }
    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
    public long getClicks() {
        return clicks;
    }
    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }
    public List<LinkStatistics> getAnalytics() {
        return analytics;
    }
    public void setAnalytics(List<LinkStatistics> analytics) {
        this.analytics = analytics;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getPageTitle() {
        return pageTitle;
    }
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }
    public long getExpireTime() {
        return expireTime;
    }
    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
    public long getExpireClicks() {
        return expireClicks;
    }
    public void setExpireClicks(int expireClicks) {
        this.expireClicks = expireClicks;
    }
    public long getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
