/*
 * Copyright 2012 FuseSource
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
package com.fusesource.demo.controller;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/controller/")
public class Controller {

    private String controllerName;

    public Controller(String controllerName) {
        this.controllerName = controllerName;
    }

    @POST
    @Path("/process/")
    public String process(String requestData) {
        System.out.println(controllerName + " request data: " + requestData);
        String responseData = requestData.replace("</data>", "<controller>" + controllerName + "</controller></data>");
        System.out.println(controllerName + " response data: " + responseData);
        return responseData;
    }
}
