package org.javaswift.joss.command.impl.object;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.FileEntity;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.object.AutoExtractCommand;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.StoredObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class AutoExtractCommandImpl extends AbstractObjectCommand<HttpPut, Object> implements AutoExtractCommand {
    public AutoExtractCommandImpl(Account account, HttpClient httpClient, Access access, StoredObject targetObject, File archive, String archiveFormat) {
        super(account, httpClient, access, targetObject);
        prepareRequest(archive, archiveFormat);
    }

    private void prepareRequest(File archive, String archiveFormat) {
        try {
            this.request.setEntity(new FileEntity(archive));
            URI newUri = new URIBuilder(this.request.getURI()).addParameter("extract-archive", archiveFormat).build();
            this.request.setURI(newUri);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error while constructing URI.", e);
        }
    }

    @Override
    protected HttpPut createRequest(String url) {
        return new HttpPut(url);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[]{
                new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_CREATED)),
                new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_OK))
        };
    }

}
