/*
 * Copyright 2024 Patriot project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.patriot_framework.generator.controll.server.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.patriot_framework.generator.dataFeed.ConstantDataFeed;
import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.device.impl.basicSensors.DHT11;
import io.patriot_framework.generator.device.impl.basicSensors.Thermometer;
import io.patriot_framework.generator.utils.JSONSerializer;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataFeedResourceTest {
    String ip = "127.0.0.1";
    String port = "5683";
    String rootResource = "/";
    // Define the CoAP server URI

    String t1Resource = "/sensor/t1";
    String coapUrl = "coap://" + ip +":" + port ;
    String t1uri = coapUrl + t1Resource;

    Thermometer t1;

    CoapClient client;


    private DataFeed createConstantDataFeed(double value, String label) {
        DataFeed dataFeed = new ConstantDataFeed(value);
        dataFeed.setLabel(label);
        return dataFeed;
    }

    private String buildRequestUri(String path, String label) {
        return coapUrl + path + "/" + label;
    }

    private String buildRequestUri(String path) {
        return coapUrl + path;
    }

    private CoapResponse sendPutRequest(CoapClient client, String payload) throws ConnectorException, IOException {
        return client.put(payload, MediaTypeRegistry.APPLICATION_JSON);
    }

    private CoapResponse sendPostRequest(CoapClient client, String payload) throws ConnectorException, IOException {
        return client.post(payload, MediaTypeRegistry.APPLICATION_JSON);
    }


    @BeforeEach
    public void serverSetup() {
        DataFeed df = new ConstantDataFeed(25);
        df.setLabel("df1");
        t1 = new Thermometer("t1", df);
        t1.registerToCoapServer();

        client = new CoapClient(coapUrl + rootResource);
    }

    @AfterEach
    public void shutdownClient (){
        // Shutdown the client
        client.shutdown();
    }


    @Test
    public void getAllDataFeeds() throws ConnectorException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        client.setURI(t1uri + "/dataFeed");
        CoapResponse response = client.get();
        assertEquals(CoAP.ResponseCode.CONTENT, response.getCode());
        assertEquals(objectMapper.writeValueAsString(t1.getDataFeeds()), response.getResponseText());
    }


    @Test
    public void getDataFeed() throws ConnectorException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String label = "df1";
        String unknownLabel = "unknownLabel";
        String uri = buildRequestUri( t1Resource +"/dataFeed", unknownLabel);
        client.setURI(uri);
        CoapResponse response = client.get();
        assertEquals(CoAP.ResponseCode.NOT_FOUND, response.getCode());
        System.out.println( response.getResponseText());
        uri = buildRequestUri(t1Resource + "/dataFeed", label);
        client.setURI(uri);
        response = client.get();
        assertEquals(CoAP.ResponseCode.CONTENT, response.getCode());
        assertEquals(objectMapper.writeValueAsString(t1.getDataFeed()), response.getResponseText());
    }


    @Test
    public void postSimpleDataFeed() throws ConnectorException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String anotherLabel = "anotherLabel";

        DataFeed df1 = new ConstantDataFeed(25);
        df1.setLabel("df1");
        DataFeed df2 = new ConstantDataFeed(25);
        df2.setLabel("df2");
        DataFeed df3 = new ConstantDataFeed(25);
        df3.setLabel("df3");
        var dht = new DHT11("dht", df1, df2);
        dht.registerToCoapServer();

        CoapResponse response;

        String requestBody =  JSONSerializer.serializeDataFeed(df3);
        String uri = coapUrl + "/sensor/dht/dataFeed";
        client.setURI(uri);
        response = sendPostRequest(client, requestBody);
        assertEquals(CoAP.ResponseCode.CREATED, response.getCode());

        response = sendPostRequest(client, requestBody);
        assertEquals(CoAP.ResponseCode.CONFLICT, response.getCode());
        assertEquals("Data feed with given label already exists", response.getResponseText());

        df3.setLabel(anotherLabel);
        requestBody =  JSONSerializer.serializeDataFeed(df3);
        client.setURI(coapUrl + "/sensor/t1/dataFeed");
        response = sendPostRequest(client, requestBody);
        assertEquals(CoAP.ResponseCode.CONFLICT, response.getCode());
        assertEquals("Simple sensor already contains data feed", response.getResponseText());

        // assert that new data-feed path was created
        df3.setLabel("df3");
        String getUri = buildRequestUri("/sensor/dht/dataFeed", df3.getLabel());
        client.setURI(getUri);
        response = client.get();
        assertEquals(CoAP.ResponseCode.CONTENT, response.getCode());
        assertEquals(objectMapper.writeValueAsString(df3), response.getResponseText());
    }

    @Test
    public void putSimpleDataFeed() throws ConnectorException, IOException {
        double newTemperature = 42.0;
        String dataFeedLabel = t1.getDataFeed().getLabel();
        String unknownLabel = "unknownLabel";

        DataFeed newDataFeed = createConstantDataFeed(newTemperature, unknownLabel);
        String requestBody =  JSONSerializer.serializeDataFeed(newDataFeed);
        String requestUri = buildRequestUri("/sensor/t1/dataFeed", dataFeedLabel);
        client.setURI(requestUri);

        CoapResponse response = sendPutRequest(client, requestBody);
        assertEquals(CoAP.ResponseCode.UNPROCESSABLE_ENTITY, response.getCode());
        assertEquals("DataFeed must have same label", response.getResponseText());

        newDataFeed.setLabel(dataFeedLabel);
        requestBody =  JSONSerializer.serializeDataFeed(newDataFeed);
        response = sendPutRequest(client, requestBody);
        assertEquals(CoAP.ResponseCode.CHANGED, response.getCode());
        assertEquals(newTemperature, t1.requestData().get(0).get(Double.class));
    }

    @Test
    public void deleteSimpleDataFeed() throws ConnectorException, IOException {
        String dataFeedLabel = t1.getDataFeed().getLabel();
        String requestUri = buildRequestUri("/sensor/t1/dataFeed", dataFeedLabel);
        client.setURI(requestUri);
        CoapResponse response = client.delete();
        assertEquals(CoAP.ResponseCode.DELETED, response.getCode());
        assertEquals(0, t1.getDataFeeds().size());
    }
}