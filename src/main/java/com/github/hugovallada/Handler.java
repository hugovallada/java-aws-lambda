package com.github.hugovallada;

import java.io.IOException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hugovallada.dto.LoginRequest;
import com.github.hugovallada.dto.LoginResponse;

public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context ctx) {
        try {
            final var logger = ctx.getLogger();
            final var body = request.getBody();
            logger.log("Request received - " + body);
            var loginRequest = mapper
                    .readValue(body, LoginRequest.class);
            var isAuthorized = false;
            if (loginRequest.username().equalsIgnoreCase("admin") && loginRequest.password().equals("123")) {
                isAuthorized = true;
            }
            final var loginResponse = new LoginResponse(isAuthorized);
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(mapper.writeValueAsString(loginResponse));
        } catch (IOException ex) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500);
        }
    }

}
