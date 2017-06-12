package org.hspconsortium.client.session;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;

import java.net.ProxySelector;

public class ApacheHttpClientFactory {

    private String proxyHost;
    private Integer proxyPort;
    private String proxyUser;
    private String proxyPassword;
    private Integer httpConnectionTimeOut;
    private Integer httpReadTimeOut;

    public ApacheHttpClientFactory(String proxyHost, Integer proxyPort, String proxyUser, String proxyPassword, Integer httpConnectionTimeOut, Integer httpReadTimeOut) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUser = proxyUser;
        this.proxyPassword = proxyPassword;
        this.httpConnectionTimeOut = httpConnectionTimeOut;
        this.httpReadTimeOut = httpReadTimeOut;
    }

    public CloseableHttpClient getClient() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(httpConnectionTimeOut)
                .setConnectionRequestTimeout(httpReadTimeOut).build();

        BasicCredentialsProvider credsProvider = null;
        if (proxyUser != null) {
            credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(proxyHost, proxyPort),
                    new UsernamePasswordCredentials(proxyUser, proxyPassword)
            );
        }

        return HttpClientBuilder.create()
                .setRoutePlanner(new SystemDefaultRoutePlanner(ProxySelector.getDefault()))
                .setDefaultCredentialsProvider(credsProvider)
                .setDefaultRequestConfig(config).build();
    }
}
