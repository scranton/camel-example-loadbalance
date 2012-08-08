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

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

public class Server {

    private static final String CONTROLLER_NAME = System.getProperty("controller.name");
    private static final String PORT = System.getProperty("controller.port");

    protected Server() throws Exception {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(Controller.class);
        sf.setResourceProvider(Controller.class, new SingletonResourceProvider(new Controller(CONTROLLER_NAME)));
        sf.setAddress("http://localhost:" + PORT + "/");
        sf.create();
    }

    public static void main(String args[]) throws Exception {
        new Server();
        System.out.println(CONTROLLER_NAME + " is ready and listening on port " + PORT);
        Thread.sleep(24 * 60 * 60 * 1000);
        System.out.println(CONTROLLER_NAME + " exiting");
        System.exit(0);
    }
}
