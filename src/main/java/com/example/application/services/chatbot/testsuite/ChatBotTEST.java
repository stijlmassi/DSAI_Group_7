package com.example.application.services.chatbot.testsuite;

import com.example.application.services.ChatBot;
import com.example.application.services.chatbot.CFG;
import com.example.application.services.chatbot.CNF;
import com.example.application.services.chatbot.CYK;
import com.example.application.services.chatbot.Rule;
import org.junit.jupiter.api.Test;

public class ChatBotTEST {

    @Test
    public void givesAnswer(){
        ChatBot.init();
//        System.out.println(ChatBot.respondTo("When do I have to hand in the report for Discrete Mathematics "));
        System.out.println(ChatBot.respondTo("How many electives can I get "));

        System.out.println(ChatBot.respondTo("When is the deadline for the project code submission"));
        System.out.println(ChatBot.respondTo("What is the website of Incognito"));

        assert ChatBot.respondTo("Which lectures are there on Monday at 9").equals("We start the week with math ");
        assert ChatBot.respondTo("Who teaches Discrete Mathematics").equals("Steven Kelk ");
        assert ChatBot.respondTo("Do I have to take the exams online").equals("No, you can take them on campus, as of now ");
//        assert ChatBot.respondTo("How many credits do I need to get the BSA").equals("42 ");
        assert ChatBot.respondTo("What class do I have this semester").equals("Human computer interactions, Philosophy and AI, Mathematical Modeling, and project ");
        assert ChatBot.respondTo("Who can I talk to if I have personal problems").equals("Tessa can guide you to qualified professional for your problems ");
    }


    @Test
    public void notGivesAnswer(){
        ChatBot.init();
//        System.out.println(ChatBot.respondTo("Which lectures are there on Wednesday at 9"));
        assert ChatBot.respondTo("Which lectures are there on Wednesday at 9").equals("I don't know");
    }
}
