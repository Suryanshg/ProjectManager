package projectmanager.handler;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import projectmanager.db.TeammateDAO;
import projectmanager.http.CreateTeammateRequest;
import projectmanager.http.CreateTeammateResponse;
import projectmanager.model.Teammate;

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
        String SAMPLE_INPUT_STRING = "{\"name\": \"Test Teammate 2\", \"projectid\": \"0bc22c80-a9d6-43a1-b1f2-7fba045eae0b\"}";
        int RESULT = 200;

        try {
            testInput(SAMPLE_INPUT_STRING, RESULT);
        } catch(Exception ioe) {
            Assert.fail("invalid: " + ioe.getMessage());
        }
    }


    @Test
    public void createTeammateTestFails() throws Exception {
    	
    	// Let us create a Teammate with the same name and project id
    	TeammateDAO dao = new TeammateDAO();
    	Teammate t = new Teammate("Test Teammate");
    	dao.addTeammate(t, "0bc22c80-a9d6-43a1-b1f2-7fba045eae0b");
    	
        String SAMPLE_INPUT_STRING = "{\"name\": \"Test Teammate\", \"projectid\": \"0bc22c80-a9d6-43a1-b1f2-7fba045eae0b\" }";
        int RESULT = 422;

        try {
            testInput(SAMPLE_INPUT_STRING, RESULT); 
            dao.deleteTeammate(t.id.toString());
        } catch (Exception ioe) {
            Assert.fail("invalid: " + ioe.getMessage());
        }
    }

}