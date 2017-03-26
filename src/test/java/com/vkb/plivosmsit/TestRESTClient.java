package com.vkb.plivosmsit;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class TestRESTClient {
    String[] getRESTCallResponse (String requestStr, String username, String password, String uri) throws IOException {

        String[] RESTresponse = makeRESTCall(requestStr, username, password, uri);
        return processResponse(RESTresponse);
    }

    private String [] processResponse(String[] RESTresponse) {
        int responseCode = new Integer(RESTresponse[0]);
        String responsePhrase = RESTresponse[1];
        String responseStr = RESTresponse[2];

        if(HttpStatus.SC_OK == responseCode) {
            return processGoodResponse(responseStr);
        }
        else {
            return processErrorResponse(responseCode, responsePhrase);
        }
    }

    private String[] processGoodResponse(String responseStr) {
        String [] retArr = new String [2];

        try {
            JSONParser parser = new JSONParser();
            JSONObject obj  = (JSONObject) parser.parse(responseStr);

            String message = (String) obj.get("message");
            String error = (String) obj.get("error");

            retArr[0] = message;
            retArr[1] = error;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return retArr;
    }

    private String [] processErrorResponse(int errorCode, String errorMsg) {
        String [] retArr = new String [2];

        retArr[0] = "" + errorCode;
        retArr[1] = errorMsg;

        return retArr;
    }

    private String[] makeRESTCall(String requestStr, String username, String password, String uri) throws IOException {
        HttpPost post = new HttpPost(uri);

        StringEntity input = new StringEntity(requestStr);
        input.setContentType("application/json");
        post.setEntity(input);

        String authString = username + ":" + password;
        String encodedAuth = new BASE64Encoder().encode(authString.getBytes());
        post.addHeader("Authorization","Basic " + encodedAuth);

        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line;
        String retLine = "";
        while ((line = rd.readLine()) != null) {
            retLine += line;
        }

        String [] responseStrArr = new String[3];
        responseStrArr[0] = "" + response.getStatusLine().getStatusCode();
        responseStrArr[1] = response.getStatusLine().getReasonPhrase();
        responseStrArr[2] = retLine;

        return responseStrArr;
    }
}
