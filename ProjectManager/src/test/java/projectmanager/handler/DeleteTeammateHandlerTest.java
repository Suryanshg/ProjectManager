package projectmanager.handler;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import projectmanager.db.ProjectDAO;
import projectmanager.db.TeammateDAO;
import projectmanager.http.*;
import projectmanager.model.Teammate;

public class DeleteTeammateHandlerTest extends LambdaTest {

    void testInput(String incoming, int outgoing, boolean deleted) throws Exception {
        DeleteTeammateHandler handler = new DeleteTeammateHandler();
        DeleteTeammateRequest req = new Gson().fromJson(incoming, DeleteTeammateRequest.class);
        DeleteTeammateResponse response = handler.handleRequest(req, createContext("delete teammate"));

        assertEquals(outgoing, response.statusCode);
        assertEquals(deleted, response.deleted);

    }

    @Test
    public void deleteTeammateTestPasses() throws Exception {
        // Make a dummy teammate
        TeammateDAO dao = new TeammateDAO();
        dao.addTeammate(new Teammate("TestDelete Teammate"), "0bc22c80-a9d6-43a1-b1f2-7fba045eae0b");

        Teammate teammate = dao.getTeammateByNameAndProjectId("TestDelete Teammate", "0bc22c80-a9d6-43a1-b1f2-7fba045eae0b");

        String teammateid = teammate.id.toString();

        String SAMPLE_INPUT_STRING = "{\"teammateid\": \"" + teammateid + "\"}";
        int RESULT = 200;

        try {
            testInput(SAMPLE_INPUT_STRING, RESULT, true);
        } catch(Exception ioe) {
            Assert.fail("invalid: " + ioe.getMessage());
        }
    }


    @Test
    public void deleteTeammateTestFails() {
        String teammateid = "accordingtoallknownlawsofaviationthereisanowayabeeshouldbeabletofly";
        String SAMPLE_INPUT_STRING = "{\"teammateid\": \"" + teammateid + "\"}";
        int RESULT = 422;

        try {
            testInput(SAMPLE_INPUT_STRING, RESULT, false);
        } catch (Exception ioe) {
            Assert.fail("invalid: " + ioe.getMessage());
        }
    }

}