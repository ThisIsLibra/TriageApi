/*
 * Copyright (C) 2020 Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package triageapi.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * This class is used to send HTTP requests to a given URL. At the creation of
 * the object, the Triage API key is required. This key is then used in all
 * following requests that are made with that specific instance.
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 */
public class TriageConnector {

    /**
     * The API key of the used Triage account
     */
    private String key;

    /**
     * Creates an instance of the connector, which is used to send the HTTP
     * requests to the given URLs
     *
     * @param key the Triage key to authenticate the request with the Triage API
     */
    public TriageConnector(String key) {
        //Sets the API key
        this.key = key;
    }

    /**
     * Checks if the status code is below 100 (which is not an offical status
     * code) or 400 or higher. The 400 range of status codes refers to client
     * errors, whereas the 500 range refers to server errors.
     *
     * @param url the URL which was requested
     * @param statusCode the server's status code in the given response
     * @throws IOException if the status code is lower than 100, or above (or
     * equal to) 400
     */
    private void checkStatusCode(String url, int statusCode) throws IOException {
        if (statusCode < 100 || statusCode >= 400) {
            throw new IOException("Status code error: the response of \"" + url + "\" returned " + statusCode);
        }
    }

    /**
     * Performs a generic HTTP GET request to the given URL. The response is
     * returned as a byte array, which can be converted into several data types,
     * depending on the expected outcome.
     *
     * @param url the URL to request
     * @return the web server's response in the form of a byte array
     * @throws IOException if anything goes wrong with the HTTP GET connection
     */
    public byte[] get(String url) throws IOException {
        //Create the request based on the URL
        HttpGet request = new HttpGet(url);
        //Set the authorisation bearer header
        request.setHeader("Authorization", "Bearer " + key);
        //Create a HTTP client
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //Execute the request
        CloseableHttpResponse response = httpClient.execute(request);
        //Get the response
        HttpEntity responseEntity = response.getEntity();

        //Check if the status code indicates an error
        checkStatusCode(url, response.getStatusLine().getStatusCode());

        //Read the response, although the size is unknown, its read in chunks of 1024 bytes
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int offset;
        byte[] data = new byte[1024];
        while ((offset = responseEntity.getContent().read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, offset);
        }
        //Flush the buffer
        buffer.flush();
        //Return the byte array
        return buffer.toByteArray();
    }

    /**
     * Performs a generic HTTP POST request based on the given request. The
     * response is returned as a byte array, which can be converted into several
     * data types, depending on the expected outcome.
     *
     * @param url the URL to send the POST request to
     * @param json the JSON body of the post request
     * @return the web server's response in the form of a byte array
     * @throws IOException if anything goes wrong with the HTTP POST connection
     */
    public byte[] post(String url, String json) throws IOException {
        StringEntity entity = new StringEntity(json);
        //Create a HTTP client
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //Create a HTTP post object for the given URL
        HttpPost httpPost = new HttpPost(url);
        //Add the API key to the request
        httpPost.setHeader("Authorization", "Bearer " + key);
        //Set the type to JSON
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(entity);
        //Execute the HTTP POST request
        CloseableHttpResponse response = httpClient.execute(httpPost);
        //Get the response
        HttpEntity responseEntity = response.getEntity();

        //Check if the status code indicates an error
        checkStatusCode(url, response.getStatusLine().getStatusCode());

        //Read the response in chunks of 1024 bytes
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int offset;
        byte[] data = new byte[1024];
        while ((offset = responseEntity.getContent().read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, offset);
        }
        //Flush the buffer
        buffer.flush();
        //Return the byte array
        return buffer.toByteArray();

    }

    /**
     * Performs a generic HTTP POST request based on the given request. The
     * response is returned as a byte array, which can be converted into several
     * data types, depending on the expected outcome.
     *
     * @param url the URL to send the POST request to
     * @param builder the Multipart Entity Builder that contains the body of the
     * POST request
     * @return the web server's response in the form of a byte array
     * @throws IOException if anything goes wrong with the HTTP POST connection
     */
    public byte[] post(String url, MultipartEntityBuilder builder) throws IOException {
        //Create a HTTP client
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //Create a HTTP post object for the given URL
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", "Bearer " + key);
        //Get the multipart builder's build
        HttpEntity multipart = builder.build();
        //Set the newly built multipart object
        httpPost.setEntity(multipart);
        //Execute the HTTP POST request
        CloseableHttpResponse response = httpClient.execute(httpPost);
        //Get the response
        HttpEntity responseEntity = response.getEntity();

        //Check if the status code indicates an error
        checkStatusCode(url, response.getStatusLine().getStatusCode());

        //Read the response in chunks of 1024 bytes
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int offset;
        byte[] data = new byte[1024];
        while ((offset = responseEntity.getContent().read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, offset);
        }
        //Flush the buffer
        buffer.flush();
        //Return the byte array
        return buffer.toByteArray();

    }
}
