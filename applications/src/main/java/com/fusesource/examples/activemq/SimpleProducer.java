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

public class SimpleProducer {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleProducer.class);

    private static final Boolean NON_TRANSACTED = false;
    private static final long MESSAGE_TIME_TO_LIVE_MILLISECONDS = 0;
    private static final int MESSAGE_DELAY_MILLISECONDS = 500;
    private static final int NUM_MESSAGES_TO_BE_SENT = 1000 * 1000;
    private static final String CONNECTION_FACTORY_NAME = "myJmsFactory";
    private static final String DESTINATION_NAME = "queue/input";

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
            MessageProducer producer = session.createProducer(destination);

            producer.setTimeToLive(MESSAGE_TIME_TO_LIVE_MILLISECONDS);

            for (int i = 1; i <= NUM_MESSAGES_TO_BE_SENT; i++) {
                StringBuilder data = new StringBuilder();
                data.append("<data><messageNumber>");
                data.append(i);
                data.append("</messageNumber><timestamp>");
                data.append(System.currentTimeMillis());
                data.append("</timestamp></data>");
                TextMessage message = session.createTextMessage(data.toString());
                LOG.info("Sending message {} with data: {}", i, data);
                producer.send(message);

                Thread.sleep(MESSAGE_DELAY_MILLISECONDS);
            }

            // Cleanup
            producer.close();
            session.close();
        } catch (Throwable t) {
            LOG.error("Error producing message", t);
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
