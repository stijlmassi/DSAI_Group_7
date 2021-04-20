package com.example.application.views.chatbotview;

import com.example.application.services.ChatBot;
import com.example.application.services.camera.Camera;
import com.example.application.services.phase1chatbot.SkillParser;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.component.html.Image;


@Route(value = "chatbot", layout = MainView.class)
@PageTitle("ChatBot")
@CssImport("./styles/views/chatbot/chatbot.css")
@RouteAlias(value = "", layout = MainView.class)
public class ChatBotView extends HorizontalLayout {

    private TextField questionTextField;
    private Dialog cameraPopUp = new Dialog();
    private Button clearButton = new Button("Clear Chat");
    private Button snapshot = new Button("Snapshot!");
    private Button cameraCheck = new Button("Camera Check");
    private H4 thinking = new H4("ChatBot: Mmmm... Let me think.");
    private TextArea area = new TextArea();
    private ChatBot chatBot = new ChatBot();
    private String conversation = "";
    private SkillParser skillParser = new SkillParser();

    public ChatBotView() {
        setId("chatbot-view");
        area.setReadOnly(true);
        area.setId("text-area");
        questionTextField = new TextField("Ask me anything");
        questionTextField.setId("question-field");
        clearButton.setId("clear-button");
        cameraCheck.setId("camera-button");
        cameraPopUp.setWidth("500px");
        cameraPopUp.setHeight("500px");
        questionTextField.setEnabled(false);
        questionTextField.addKeyPressListener(Key.ENTER, e -> {
            //disable Text Field while ChatBot is thinking
            questionTextField.setEnabled(false);
            //display question in H4
            String questionH4 = "You: " + questionTextField.getValue();
            conversation += questionH4 + "\n";
            //display "thinking" while ChatBot is thinking
            add(thinking);
            //get response from chatBot
            String responseString = skillParser.answer(questionTextField.getValue());
            String responseH4 = "ChatBot: " + responseString;
            conversation += responseH4 + "\n";
            area.setValue(conversation);
            //clearButton Text Field
            questionTextField.clear();
            //re-enable Text Field
            questionTextField.setEnabled(true);
            remove(thinking);
            clearButton.setEnabled(true);
        });
        add(cameraCheck);
        add(questionTextField);
        add(clearButton);
        add(area);
        clearButton.addClickListener(e -> {
            conversation = "";
            area.setValue(conversation);
            clearButton.setEnabled(false);
        });
        clearButton.setEnabled(false);

        cameraCheck.addClickListener(e -> {
            questionTextField.setEnabled(true);
            snapshot.setId("snapshot-button");
            cameraPopUp.add(snapshot);
            cameraPopUp.open();
        });

        snapshot.addClickListener(e -> {
            cameraPopUp.removeAll();
            Camera camera = new Camera();
            StreamResource streamResource = camera.createImage();
            Image cameraPic = new Image(streamResource,"capture");
            cameraPic.setId("camera-frame");
            cameraPopUp.add(cameraPic);
        });


    }


}
