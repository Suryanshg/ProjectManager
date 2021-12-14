package projectmanager.handler;

import static org.junit.Assert.*;
import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Test;

import projectmanager.handler.AssignTeammateHandler;
import projectmanager.http.AssignTeammateRequest;
import projectmanager.http.AssignTeammateResponse;

import projectmanager.handler.UnassignTeammateHandler;
import projectmanager.http.UnassignTeammateRequest;
import projectmanager.http.UnassignTeammateResponse;

public class AssignUnassignTeammateHandlerTest extends LambdaTest {
  void testAssignInput(String incoming, int outgoing) throws Exception {
    AssignTeammateHandler handler = new AssignTeammateHandler();
    AssignTeammateRequest req = new Gson().fromJson(incoming, AssignTeammateRequest.class);
    AssignTeammateResponse response = handler.handleRequest(req, createContext("create teammate"));

    assertEquals(outgoing, response.statusCode);
  }

  void testUnassignInput(String incoming, int outgoing) throws Exception {
    UnassignTeammateHandler handler = new UnassignTeammateHandler();
    UnassignTeammateRequest req = new Gson().fromJson(incoming, UnassignTeammateRequest.class);
    UnassignTeammateResponse response = handler.handleRequest(req, createContext("create teammate"));

    assertEquals(outgoing, response.statusCode);
  }

  @Test
  public void assignUnassignTeammateTestPasses() {
    // TODO problem occurs because project doesn't exist
    String projectid = "0bc22c80-a9d6-43a1-b1f2-7fba045eae0b";
    String taskid = "09bc2552-142b-4486-8a56-9e9cde6134d1";
    String teammateid = "9d118e43-e808-4f65-89f9-3927d6fa9960";
    String SAMPLE_INPUT_STRING = "{\"projectid\": \"" + projectid + "\", \"taskid\": \"" + taskid
        + "\", \"teammateid\": \"" + teammateid + "\" }";
    int RESULT = 200;

    try {
      testAssignInput(SAMPLE_INPUT_STRING, RESULT);
      testUnassignInput(SAMPLE_INPUT_STRING, RESULT);
    } catch (Exception ioe) {
      Assert.fail("invalid: " + ioe.getMessage());
    }
  }
}
