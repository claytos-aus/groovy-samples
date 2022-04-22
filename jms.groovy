#!/usr/bin/env groovy
// https://gist.github.com/welshstew/45aa6f42f847fce25593
@Grab('org.apache.activemq:activemq-all:5.17.0')
@Grab('org.slf4j:slf4j-simple:1.7.36')

import org.apache.activemq.ActiveMQConnectionFactory
import javax.jms.*

def brokerUrl = 'tcp://localhost:61616'
def queue = 'the.soapoverjms.service'
def soapAction = 'urn:sixtree:soapoverjms:dosomething'

def reader = new BufferedReader(new InputStreamReader(System.in))

new ActiveMQConnectionFactory(brokerURL: brokerUrl).createConnection().with {
  start()
  createSession(false, Session.AUTO_ACKNOWLEDGE).with {
   def message = createTextMessage(reader.text)
   message.with {
    JMSDeliveryMode = DeliveryMode.NON_PERSISTENT
    // JMSReplyTo = createTemporaryQueue()
    JMSReplyTo = createQueue('DEV.QUEUE.1')
    JMSCorrelationID = UUID.randomUUID().toString()
    setStringProperty('SOAPJMS_soapAction', soapAction)
   }
    createProducer().send(createQueue(queue), message)
  }
  close()
}

//run with
//groovy jms.groovy < ~/Downloads/request.xml
