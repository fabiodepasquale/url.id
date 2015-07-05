package api;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;


public class UserInfo {
    public String browser;
    public String browserVersion;
    public String OS;
    public String ip;
    public String country;
    public String countryCode;
    public String deviceCategory;   //  detected device category
    public String userAgentType;    //  mobile browser, email client etc.
    public String producer;         //  manufacturer of an user agent

    protected void getUserAgent(HttpServletRequest request) throws ServletException, IOException {

        // Get an UserAgentStringParser and analyze the requesting client
        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
        ReadableUserAgent agent = parser.parse(request.getHeader("User-Agent"));

        this.browser = agent.getName();
        this.browserVersion = agent.getVersionNumber().toVersionString();
        this.OS = agent.getOperatingSystem().getName();
        this.deviceCategory = agent.getDeviceCategory().getName();
        this.userAgentType = agent.getTypeName();
        this.producer = agent.getProducer();
        this.ip = request.getRemoteAddr();
    }

    protected void getCountryCode(String ip) throws IOException {
        File database = new File("./GeoLite2-Country.mmdb");

        DatabaseReader reader = new DatabaseReader.Builder(database).build();
        InetAddress ipAddress = InetAddress.getByName(ip);

        try {
            CountryResponse response = reader.country(ipAddress);
            Country country = response.getCountry();
            this.countryCode = country.getIsoCode();            // 'US'
            this.country = country.getName();                   // 'United States'
        } catch (GeoIp2Exception e) {
            this.countryCode = "Unknown";
            this.country = "Unknown";
        }
    }
}