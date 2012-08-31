package nl.t42.openstack.command;

import nl.t42.openstack.BaseCommandTest;
import nl.t42.openstack.model.access.Access;
import nl.t42.openstack.util.ClasspathTemplateResource;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.when;

public class CreateContainerCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void createContainerSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        new CreateContainerCommand(httpClient, defaultAccess, "containerName").execute();
    }

    @Test
    public void createContainerFail() throws IOException {
        checkForError(202, new CreateContainerCommand(httpClient, defaultAccess, "containerName"), CommandExceptionError.CONTAINER_ALREADY_EXISTS);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new CreateContainerCommand(httpClient, defaultAccess, "containerName"), CommandExceptionError.UNKNOWN);
    }
}
