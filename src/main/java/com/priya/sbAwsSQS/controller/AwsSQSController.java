package com.priya.sbAwsSQS.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.sqs.model.Message;


@RestController
@RequestMapping(value = "/sqs")
public class AwsSQSController {
      
	Logger logger= LoggerFactory.getLogger(AwsSQSController.class);
	 
	@Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Value("${cloud.aws.uri}")
    private String endpoint;
    
    private static final String QUEUE = "rani-queue";
    
    /* Note: Here in this example we are printing the message on the console and the
     *  message will be deleted from the queue once it is successfully delivered in way2 and it will be wont be deleted in way1
     * So comment the method annotated with @SqsListener to test in the AWS console
     */
   
      //Note2: only need EC2 access 

    //way1 : Message can be sent by specifying the queue endpoint uri in the properties file
    /*@GetMapping("/send/{message}")
    public void sendMessageToQueue(@PathVariable String message) {
        queueMessagingTemplate.send(endpoint, MessageBuilder.withPayload(message).build());
    }
    
    @SqsListener(value=QUEUE,deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void loadMessageFromSQS(String message)  {
        logger.info("message from SQS Queue {}",message);
    }
    */
    
   //way2 : To send and receive (Message can be sent by specifying the queue name) and Message parameter
    
   /* HTTP POST url - http://localhost:10091/sqs/newSend
    @ResponseStatus annotation marks the method with the status-code and the reason message that should be returned.
    */
    
   
    @PostMapping(value = "/newSend")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void sendMessageToSqs(@RequestBody Message message) {
    	logger.info("Sending the message to the Amazon sqs.");
        queueMessagingTemplate.convertAndSend(QUEUE, message);
        logger.info("Message sent successfully to the Amazon sqs.");
    }
    
    
    // @SqsListener listens to the message from the specified queue.
    @SqsListener(value = QUEUE, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void getMessageFromSqs(Message message, @Header("MessageId") String messageId) {
    	logger.info("Received message= {} with messageId= {}", message.getBody(), messageId);
        // TODO - Developer can do some operations like saving the message to the database, calling any 3rd party etc.
    } 

  
}
