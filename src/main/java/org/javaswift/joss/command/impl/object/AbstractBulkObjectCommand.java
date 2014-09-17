package org.javaswift.joss.command.impl.object;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.javaswift.joss.command.impl.core.AbstractSecureCommand;
import org.javaswift.joss.command.shared.core.Command;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;

public abstract class AbstractBulkObjectCommand<M extends HttpRequestBase, N> extends AbstractSecureCommand<M, N> {

    public AbstractBulkObjectCommand(Account account, HttpClient httpClient, Container container, Access access) {
        super(account, httpClient, getURL(access, container), access.getToken());
    }
}

