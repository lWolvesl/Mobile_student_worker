package com.li.dao;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

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
        Set<Cookie> cookies = new HashSet<>();
        cookies.add(new Cookie(properties.getProperty("host"),properties.getProperty("name"),properties.getProperty("cookiee")));
        Iterator<Cookie> i = cookies.iterator();
        while (i.hasNext())
        {
            webClient.getCookieManager().addCookie(i.next());
        }
    }

    public WebClient getWebClient() {
        return webClient;
    }
}
