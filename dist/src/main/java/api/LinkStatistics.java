package api;


import java.util.List;

public class LinkStatistics {

    private List<StatisticsEntity> browsers;
    private List<StatisticsEntity> countries;
    private List<StatisticsEntity> platforms;

    public LinkStatistics(List<StatisticsEntity> countries, List<StatisticsEntity> browsers, List<StatisticsEntity> platforms) {
        this.countries = countries;
        this.browsers = browsers;
        this.platforms = platforms;
    }

    public List<StatisticsEntity> getCountries() {
        return countries;
    }
    public void setCountries(List<StatisticsEntity> countries) {
        this.countries = countries;
    }
    public List<StatisticsEntity> getBrowsers() {
        return browsers;
    }
    public void setBrowsers(List<StatisticsEntity> browsers) {
        this.browsers = browsers;
    }
    public List<StatisticsEntity> getPlatforms() {
        return platforms;
    }
    public void setPlatforms(List<StatisticsEntity> platforms) {
        this.platforms = platforms;
    }

}