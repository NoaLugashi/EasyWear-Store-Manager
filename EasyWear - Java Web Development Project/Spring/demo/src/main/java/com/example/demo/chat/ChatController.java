package com.example.demo.chat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.demo.User.User;
import com.example.demo.User.UserRepository;
import com.example.demo.employee.Employee;
import com.example.demo.employee.EmployeeRepository;
import com.example.demo.log.AuditLogUtil;
import com.example.demo.log.LogService;

@Controller
public class ChatController {
    private final EmployeeRepository employeeRepository;
    private final LogService logService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private final Map<String, Queue<String>> senderBranchQueue = new HashMap<>();
    private Queue<String> queue;
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    public ChatController(EmployeeRepository employeeRepository, LogService logService, SimpMessagingTemplate messagingTemplate, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.logService = logService;
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
    }

    @MessageMapping("/chat.sendToBranch")
    public void sendMessageToBranch(@Payload ChatMessage chatMessage) {
        logger.info("ChetController :Processing sendMessageToBranch for sender: {}", chatMessage.getSender());
        logger.info("ChetController : the target branch is {}", chatMessage.getTargetBranch());
        logger.info("ChetController : /topic/{}", chatMessage.getTargetBranch());
        logService.addWarnLog("ChetController :Processing sendMessageToBranch for sender:");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: sendToBranch"); // Logs action with user info
        String role="";
        Optional<User> userOptional = userRepository.findByUsername(chatMessage.getSender());
        if(userOptional.isPresent()){role = userOptional.get().getRole();}
        String targetBranch = chatMessage.getTargetBranch();
        String senderBranch = chatMessage.getSenderBranch();
        queue = senderBranchQueue.get(senderBranch);
        if (queue == null) {
            logService.addWarnLog("ChetController : Queue for branch {} is null.");
            queue = new LinkedList<>();
            senderBranchQueue.put(targetBranch, queue);
            AuditLogUtil.clear();
        }
        if (queue.peek() == null) {
            logService.addWarnLog("ChetController : Queue for branch {} is empty.");
            AuditLogUtil.clear();
        } else {
            logService.addWarnLog("ChetController : First user in queue: {}");
            AuditLogUtil.clear();
        }
        if (queue.peek() != null && queue.peek().equals(chatMessage.getSender())||(role.equals("Admin"))) {
            logService.addWarnLog("ChetController : we get the right first user in queue:");
            String destination = "/topic/" + targetBranch;
            String ownDestination = "/topic/" + senderBranch ;
            messagingTemplate.convertAndSend(destination, chatMessage);
            messagingTemplate.convertAndSend(ownDestination, chatMessage);
            AuditLogUtil.clear();
        } else {
            logService.addWarnLog("ChetController : User not first in queue.");
            chatMessage.setContent("You are not first in queue. please wait!!!!!!!!");
            String ownDestination = "/topic/" + senderBranch; 
            messagingTemplate.convertAndSend(ownDestination,chatMessage);
            AuditLogUtil.clear();

        }   
    }
    @MessageMapping("/chat.getUserBranch")
    @SendTo("/topic/branch")
    public ChatMessage getUserBranch(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
    logger.info("ChatController: Processing getUserBranch for sender: {}", chatMessage.getSender());
    Optional<Employee> employeeOptional = employeeRepository.findByUsername(chatMessage.getSender());
    ChatMessage responseMessage = new ChatMessage();
    Optional<User> userOptional = userRepository.findByUsername(chatMessage.getSender());
    AuditLogUtil.setCurrentUser();
    logger.info("Action performed: getUserBranch"); // Logs action with user info
    if((userOptional.isPresent())&&(userOptional.get().getRole().equals("Admin"))){
            responseMessage.setSenderRole(userOptional.get().getRole());
            responseMessage.setSender(userOptional.get().getUsername());
            responseMessage.setSenderBranch(userOptional.get().getBranch());
            AuditLogUtil.clear();
            return responseMessage;
        }
    if (employeeOptional.isPresent()) {
        Employee employee = employeeOptional.get();
        String branch = employee.getBranch();
        if(senderBranchQueue.get(branch)!=null)
        {
            queue=senderBranchQueue.get(branch);
            logger.warn("ChatController: queue get from hashMap " + queue.toString());
            AuditLogUtil.clear();

        }
        if (queue != null && !queue.isEmpty() && senderBranchQueue.get(branch)!=null) {
            if(queue.peek().equals(chatMessage.getSender()))
            {
                responseMessage.setSenderRole(employee.getRole());
                responseMessage.setSender(employee.getusername());
                responseMessage.setSenderBranch(branch);
                AuditLogUtil.clear();
                return responseMessage;
            }
            logger.warn("ChatController: Branch {} is already occupied", branch);
            responseMessage.setContent("Branch " + branch + " is already occupied. Please wait.");
            if(!queue.peek().equals(employee.getusername()))
            {
                queue.add(employee.getusername());
                logger.warn("ChatController: user {} add to queue", employee.getusername());
                AuditLogUtil.clear();
            }
            senderBranchQueue.put(branch, queue);
            logger.info("ChetController: the queue caught, so here is the queue now: " + queue.toString());
            AuditLogUtil.clear();
            return responseMessage;
        }
        else
        {
                senderBranchQueue.put(branch, new LinkedList<>());//
                queue=senderBranchQueue.get(branch);//
                queue.add(employee.getusername());
                senderBranchQueue.put(branch, queue);
                logger.info("ChetController: the first in queue is queue is ", queue.peek());
                logger.warn("ChatController: new queue created for branch ", branch);
                responseMessage.setSenderRole(employee.getRole());
                responseMessage.setSender(employee.getusername());
                responseMessage.setSenderBranch(branch);
        
                // senderBranchQueue.get(branch).add(chatMessage.getSender());

                logService.addWarnLog("ChatController: Branch found for sender and reserved.");
                headerAccessor.getSessionAttributes().put("branch", branch);
                AuditLogUtil.clear();
                return responseMessage;
                }
        }
        
        else {
        logService.addWarnLog("ChatController: Sender not found: " + chatMessage.getSender());
        responseMessage.setContent("User not found.");
        AuditLogUtil.clear();
        return responseMessage;}
        }


        @MessageMapping("/chat.endChat")
        public void endChat(@Payload ChatMessage chatMessage) {
            logService.addWarnLog("ChatController: End chat function called for sender: {}");
            AuditLogUtil.setCurrentUser();
            logger.info("Action performed: endChat"); // Logs action with user info
            queue = senderBranchQueue.get(chatMessage.getSenderBranch());
            logger.info("ChetController: end chat for  " , queue.peek());
            if(queue.peek().equals(chatMessage.getSender())){
            queue.poll();
            logger.info("Chat ended for sender: {} in branch: {}", chatMessage.getSender(), chatMessage.getSenderBranch());
            logger.info("ChetController:the queue now after user left: " + queue.toString());
            AuditLogUtil.clear();
            }
        }
        


    @MessageMapping("/chat.addUser")
    @SendTo("/app/public")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        logger.info("ChetController :User added to chat: {}", chatMessage.getSender());
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: addUser"); // Logs action with user info
        AuditLogUtil.clear();
        return chatMessage;
    }
}