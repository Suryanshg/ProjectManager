package projectmanager.handler;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import projectmanager.db.TeammateDAO;
import projectmanager.http.CreateTeammateRequest;
import projectmanager.http.CreateTeammateResponse;

public class CreateTeammateHandlerTest extends LambdaTest {

    void testInput(String incoming, int outgoing) throws Exception {
        CreateTeammateHandler handler = new CreateTeammateHandler();
        CreateTeammateRequest req = new Gson().fromJson(incoming, CreateTeammateRequest.class);
        CreateTeammateResponse response = handler.handleRequest(req, createContext("create teammate"));

        assertEquals(outgoing, response.statusCode);

        if(response.statusCode == 200) { // Delete the newly created teammate
            TeammateDAO dao = new TeammateDAO();
            dao.deleteTeammate(response.getTeammateId());
        }

    }

    @Test
    public void createTeammateTestPasses(){
        String SAMPLE_INPUT_STRING = "{\"name\": \"Test Teammate 2\", \"projectid\": \"107d139a-9a1d-42e3-9f59-b61a93e6c7a3\"}";
        int RESULT = 200;

        try {
            testInput(SAMPLE_INPUT_STRING, RESULT);
        } catch(Exception ioe) {
            Assert.fail("invalid: " + ioe.getMessage());
        }
    }


    @Test
    public void createProjectTestFails() {
        String SAMPLE_INPUT_STRING = "{\"name\": \"Test Teammate\", \"projectid\": \"107d139a-9a1d-42e3-9f59-b61a93e6c7a3\" }";
        int RESULT = 422;

        try {
            testInput(SAMPLE_INPUT_STRING, RESULT);
        } catch (Exception ioe) {
            Assert.fail("invalid: " + ioe.getMessage());
        }
    }

}