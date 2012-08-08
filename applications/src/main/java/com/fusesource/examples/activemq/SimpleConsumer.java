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

package com.fusesource.examples.activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;

public class SimpleConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleConsumer.class);

    private static final Boolean NON_TRANSACTED = false;
    private static final String CONNECTION_FACTORY_NAME = "myJmsFactory";
    private static final String DESTINATION_NAME = "queue/output";
    private static final int MESSAGE_TIMEOUT_MILLISECONDS = 120000;

    public static void main(String args[]) {

        String brokerUrl = System.getProperty("java.naming.provider.url");
        LOG.info("******************************");
        LOG.info("Connecting to Fuse MQ Broker using URL: {}", brokerUrl);
        LOG.info("******************************");

        Connection connection = null;

        try {
            // JNDI lookup of JMS Connection Factory and JMS Destination
            Context context = new InitialContext();
            ConnectionFactory factory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
            Destination destination = (Destination) context.lookup(DESTINATION_NAME);

            connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(NON_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(destination);

            LOG.info("Start consuming messages from {} with {}ms timeout", destination.toString(), MESSAGE_TIMEOUT_MILLISECONDS);

            // Synchronous message consumer
            int i = 1;
            while (true) {
                Message message = consumer.receive(MESSAGE_TIMEOUT_MILLISECONDS);
                if (message != null) {
                    if (message instanceof TextMessage) {
                        String text = ((TextMessage) message).getText();
                        LOG.info("Got {}. message: {}", i++, text);
                    } else {
                        LOG.info(message.getClass().getName());
                        LOG.info(message.toString());
                    }
                } else {
                    break;
                }
            }

            consumer.close();
            session.close();
        } catch (Throwable t) {
            LOG.error("Error consuming message", t);
        } finally {
            // Cleanup code
            // In general, you should always close producers, consumers,
            // sessions, and connections in reverse order of creation.
            // For this simple example, a JMS connection.close will
            // clean up all other resources.
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    LOG.error("Error closing connection", e);
                }
            }
        }
    }
}