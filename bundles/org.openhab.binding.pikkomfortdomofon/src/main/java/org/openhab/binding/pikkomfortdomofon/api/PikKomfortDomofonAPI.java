/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.pikkomfortdomofon.api;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.WWWAuthenticationProtocolHandler;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpMethod;
import org.openhab.binding.pikkomfortdomofon.dto.PikKomfortDomofonBuildings;
import org.openhab.binding.pikkomfortdomofon.dto.PikKomfortDomofonIntercoms;
import org.openhab.binding.pikkomfortdomofon.dto.PikKomfortDomofonLogin;
import org.openhab.binding.pikkomfortdomofon.dto.PikKomfortDomofonProperties;
import org.openhab.binding.pikkomfortdomofon.dto.PikKomfortDomofonUsers;
import org.openhab.binding.pikkomfortdomofon.internal.PikKomfortDomofonBindingConstants;
import org.openhab.binding.pikkomfortdomofon.internal.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * The {@link PikKomfortDomofonAPI} is describing api common response.
 *
 * @author Petr Shatsillo - Initial contribution
 */
@NonNullByDefault
public class PikKomfortDomofonAPI {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final HttpClient httpClient;
    private String authHeader = "";
    @Nullable
    PikKomfortDomofonUsers users;
    @Nullable
    public PikKomfortDomofonProperties properties;
    @Nullable
    public List<PikKomfortDomofonBuildings> buildingsList = new ArrayList<>();
    @Nullable
    public List<PikKomfortDomofonIntercoms> intercomsList = new ArrayList<>();

    public PikKomfortDomofonAPI(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Boolean initialize(String username, String password) {
        String response = sendPostRequest(PikKomfortDomofonBindingConstants.PIK_INTERCOM_URL + "/api/customers/sign_in",
                new Gson().toJson(new PikKomfortDomofonLogin(username, password)), null).response;
        this.users = new Gson().fromJson(response, PikKomfortDomofonUsers.class);
        final PikKomfortDomofonUsers users = this.users;
        if (users != null) {
            if (users.getCustomerDevices().stream()
                    .noneMatch(customerDevices -> customerDevices.getModel().equals("Oh API"))) {
                int leftLimit = 48;
                int rightLimit = 122;
                int targetStringLength = 16;
                Random random = new Random();
                String uid = random.ints(leftLimit, rightLimit + 1)
                        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
                Map<String, String> params = new HashMap<>();
                params.put("customer_device[uid]", uid);
                params.put("customer_device[push_version]", "2.0.0");
                params.put("customer_device[model]", "Oh API");
                params.put("customer_device[os]", "android");
                params.put("customer_device[kind]", "mobile");
                String customerDevice = sendPostRequest(
                        PikKomfortDomofonBindingConstants.PIK_INTERCOM_URL + "/api/customers/devices", null,
                        params).response;
                logger.debug("{}", customerDevice);
                JsonObject customDeviceJson = JsonParser.parseString(customerDevice).getAsJsonObject();
                PikKomfortDomofonUsers.CustomerDevices customerDevices = new PikKomfortDomofonUsers.CustomerDevices();
                customerDevices.setId(customDeviceJson.get("id").getAsInt());
                customerDevices.setAccountId(customDeviceJson.get("account_id").getAsInt());
                customerDevices.setModel(customDeviceJson.get("model").getAsString());
                customerDevices.setKind(customDeviceJson.get("kind").getAsString());
                customerDevices.setUid(customDeviceJson.get("uid").getAsString());
                customerDevices.setOs(customDeviceJson.get("os").getAsString());
                PikKomfortDomofonUsers.CustomerDevices.SipAccount sip = new PikKomfortDomofonUsers.CustomerDevices.SipAccount();
                sip.setExUser(customDeviceJson.get("sip_account").getAsJsonObject().get("ex_user").getAsInt());
                sip.setProxy(customDeviceJson.get("sip_account").getAsJsonObject().get("proxy").getAsString());
                sip.setProxy(customDeviceJson.get("sip_account").getAsJsonObject().get("proxy").getAsString());
                sip.setPassword(customDeviceJson.get("sip_account").getAsJsonObject().get("password").getAsString());
                customerDevices.setSipAccount(sip);
                users.setCustomerDevices(customerDevices);
            }
            properties = new Gson().fromJson(
                    sendGetRequest(PikKomfortDomofonBindingConstants.PIK_INTERCOM_URL + "/api/customers/properties",
                            new Gson().toJson(new PikKomfortDomofonLogin(username, password)), null).response,
                    PikKomfortDomofonProperties.class);
            final PikKomfortDomofonProperties props = properties;
            if (props != null) {
                props.getApartments().forEach(apartments -> {
                    final List<PikKomfortDomofonBuildings> buildingsList = this.buildingsList;
                    if (buildingsList != null) {
                        PikKomfortDomofonBuildings buildingModel = new Gson().fromJson(sendGetRequest(
                                PikKomfortDomofonBindingConstants.PIK_INTERCOM_URL + "/api/buildings/"
                                        + apartments.getBuildingId(),
                                new Gson().toJson(new PikKomfortDomofonLogin(username, password)), null).response,
                                PikKomfortDomofonBuildings.class);
                        if (buildingModel != null) {
                            buildingsList.add(buildingModel);
                        } else {
                            logger.error("Cannot parse buildings");
                        }
                    }
                });
            } else {
                logger.error("Cannot find any property");
                return false;
            }
            intercomsRefresh();
        } else {
            logger.error("Cannot find any users");
            return false;
        }
        return true;
    }

    public void intercomsRefresh() {
        Type intercoms = new TypeToken<List<PikKomfortDomofonIntercoms>>() {
        }.getType();
        intercomsList = new Gson().fromJson(
                sendGetRequest(PikKomfortDomofonBindingConstants.PIK_IOT_URL + "/api/alfred/v1/personal/intercoms",
                        null, null).response,
                intercoms);
    }

    public ApiResponse sendPostRequest(String path, @Nullable String data, @Nullable Map<String, String> params) {
        @Nullable
        ContentProvider content = null;
        if (data != null) {
            content = new StringContentProvider("application/json", data, StandardCharsets.UTF_8);
        }
        HttpFields headers = new HttpFields();
        headers.add("API-VERSION", "2");
        if (!authHeader.isEmpty()) {
            headers.add("Authorization", authHeader);
        }
        return sendRequest(path, params, content, HttpMethod.POST, headers);
    }

    public ApiResponse sendGetRequest(String path, @Nullable String data, @Nullable Map<String, String> params) {
        HttpFields headers = new HttpFields();
        headers.add("API-VERSION", "2");
        if (!authHeader.isEmpty()) {
            headers.add("Authorization", authHeader);
        }
        return sendRequest(path, params, null, HttpMethod.GET, headers);
    }

    private ApiResponse sendRequest(String path, @Nullable Map<String, String> params,
            @Nullable ContentProvider content, HttpMethod method, HttpFields headers) {
        if (HttpMethod.GET.equals(method)) {
            httpClient.getProtocolHandlers().remove(WWWAuthenticationProtocolHandler.NAME);
        }
        ApiResponse result = new ApiResponse();
        Request request = httpClient.newRequest(path);
        if (headers.size() > 0) {
            request.getHeaders().addAll(headers);
        }
        request.method(method);
        if (params != null) {
            params.forEach(request::param);
        }
        try {
            if (content != null) {
                request.content(content);
            }
            ContentResponse contentResponse = null;
            contentResponse = request.send();
            result.httpCode = contentResponse.getStatus();
            if (result.httpCode == 200) {
                result.response = contentResponse.getContentAsString();
                if (contentResponse.getHeaders().get("Authorization") != null) {
                    authHeader = contentResponse.getHeaders().get("Authorization");
                }
                return result;
            }
            if (result.httpCode == 201) {
                result.response = contentResponse.getContentAsString();
                return result;
            }
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
        }
        logger.debug("send {}-request: {}", method, path);
        return new ApiResponse();
    }

    public void unlockDoor(Integer id) {
        String response = sendGetRequest(
                PikKomfortDomofonBindingConstants.PIK_IOT_URL + "/api/alfred/v1/personal/relays/" + id + "/unlock",
                null, null).response;
        logger.debug("unlock response {}", response);
    }
}
