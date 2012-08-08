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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public final class Client {

    private static final String DATA = "<data><field1>abc</field1><field2>123</field2></data>";
    private static String port = System.getProperty("controller.port");

    private Client() {
    }

    public static void main(String args[]) throws Exception {
        Client client = new Client();
        PostMethod post = new PostMethod("http://localhost:" + port + "/controller/process");
        post.addRequestHeader("Accept", "text/xml");
        RequestEntity entity = new StringRequestEntity(DATA, PostMethod.FORM_URL_ENCODED_CONTENT_TYPE, null);
        post.setRequestEntity(entity);
        HttpClient httpclient = new HttpClient();

        try {
            System.out.println("Sending data:\n" + DATA);
            int result = httpclient.executeMethod(post);
            System.out.println("Response status code: " + result);
            System.out.println("Response body: ");
            System.out.println("Response data:\n" + post.getResponseBodyAsString());
        } finally {
            post.releaseConnection();
        }

        System.out.println("\n");
        System.exit(0);
    }

}
