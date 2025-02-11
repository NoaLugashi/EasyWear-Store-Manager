package com.example.demo.chat;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;
    private String senderBranch; 
    private String targetBranch;
    private String senderRole; 
}