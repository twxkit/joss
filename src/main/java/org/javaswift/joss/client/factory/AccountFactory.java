package org.javaswift.joss.client.factory;

import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Client;
import org.javaswift.joss.client.impl.ClientImpl;
import org.javaswift.joss.client.mock.ClientMock;
import org.apache.http.client.HttpClient;

public class AccountFactory {

    private final AccountConfig config;

    private HttpClient httpClient;

    public AccountFactory(AccountConfig config) {
        this.config = config;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Account createAccount() {
        final Client client;
        if (config.isMock()) {
            client = createClientMock();
        } else {
            client = createClientImpl();
        }
        return client
                .authenticate(config.getTenant(), config.getUsername(), config.getPassword(), config.getAuthUrl())
                .setAllowReauthenticate(config.isAllowReauthenticate());
    }

    public Client createClientMock() {
        return new ClientMock()
                .setAllowEveryone(true)
                .setMillisDelay(config.getMockMillisDelay())
                .setHost(config.getHost());
    }

    public Client createClientImpl() {
        return new ClientImpl()
                .setHttpClient(this.httpClient)
                .setAllowCaching(config.isAllowCaching());
    }
}
