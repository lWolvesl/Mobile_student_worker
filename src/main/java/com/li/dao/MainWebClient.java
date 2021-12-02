package com.li.dao;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;

import java.util.Properties;

/**
 * @author li
 */
public class MainWebClient {
    private WebClient webClient;
    private Properties properties;

    public MainWebClient() {
        properties = Config.getProperties();
        webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getCookieManager().addCookie(new Cookie(properties.getProperty("host"),properties.getProperty("name"),properties.getProperty("cookie")));
    }

    public WebClient getWebClient() {
        return webClient;
    }
}
