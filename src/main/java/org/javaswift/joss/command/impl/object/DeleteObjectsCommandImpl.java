package org.javaswift.joss.command.impl.object;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.object.DeleteObjectsCommand;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class DeleteObjectsCommandImpl extends AbstractBulkObjectCommand<HttpDeleteWithBody, Object> implements DeleteObjectsCommand {

    private final Container container;
    private List<String> objectNames;

    public DeleteObjectsCommandImpl(Account account, HttpClient httpClient, Container container, Access access, List<String> objectNames) {
        super(account, httpClient, container, access);
        this.container = container;
        this.objectNames = objectNames;
        prepareRequest();
    }

    private void prepareRequest() {
        try {
            StringBuilder requestBody = new StringBuilder();
            for (String objectName : objectNames) {
                requestBody.append(container.getPath())
                        .append("/")
                        .append(objectName)
                        .append("\n");
            }

            this.request.setEntity(new StringEntity(requestBody.toString()));
            this.request.setHeader("Content-Type", "text/plain");
            this.request.setHeader("Accept", "application/json");

            URI newUri = new URIBuilder(this.request.getURI()).addParameter("bulk-delete", "").build();
            this.request.setURI(newUri);
        } catch (Exception e) {
            throw new RuntimeException("Error while preparing bulk request", e);
        }
    }


    @Override
    protected HttpDeleteWithBody createRequest(String url) {
        return new HttpDeleteWithBody(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[]{
                new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_OK))
        };
    }

    @Override
    protected Object getReturnObject(HttpResponse response) throws IOException {
        StringWriter output = new StringWriter();
        IOUtils.copy(response.getEntity().getContent(), output);
        Map result = new ObjectMapper().readValue(output.toString(), Map.class);
        return Integer.valueOf(String.valueOf(result.get("Number Deleted")));
    }
}

class HttpDeleteWithBody extends HttpPost {
    HttpDeleteWithBody(String uri) {
        super(uri);
    }

    @Override
    public String getMethod() {
        return "DELETE";
    }
}
