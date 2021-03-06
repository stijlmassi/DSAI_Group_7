package com.example.application.views.skillsview;

import com.example.application.services.phase1chatbot.Question;
import com.example.application.services.phase1chatbot.SkillParser;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Route(value = "skills", layout = MainView.class)
@CssImport("./styles/views/configurations/configurations.css")
@PageTitle("Skills Editor")
public class SkillsView extends Div {

    private final Dialog newTemplate = new Dialog();
    private final Button addTemplate = new Button("New");
    private final Button deleteEntry = new Button("Delete");
    private final Button editEntry = new Button("Edit");
    private final Button submitTemplate = new Button("Submit");
    private final Button mergeButton = new Button("Merge with other skills");
    private final Button cancelButton = new Button("Cancel");

    private final TextField request = new TextField("Request");
    private final TextField slotOne = new TextField("Slot 1");
    private final TextField slotTwo = new TextField("Slot 2");
    private final TextField slotThree = new TextField("Slot 3");
    private final TextField slotFour = new TextField("Slot 4");
    private final TextField response = new TextField("Response");
    private final MemoryBuffer buffer = new MemoryBuffer();
    private final Upload upload = new Upload(buffer);
    private final Div output = new Div();
    private final Paragraph errorText = new Paragraph();

    private final Grid<Question> grid = new Grid<>(Question.class);
    private final SkillParser jsonFile = new SkillParser();

    private String deleteQuestion;
    private Question newQuestion;


    public SkillsView() {
        setId("configurations-view");
        initDialog();
        initDeleteButton();
        initEditButton();
        initUpload();
        initGrid();
        add(new H4("Templates Editor"));
        add(grid);
        add(addTemplate, deleteEntry, editEntry);
        add(upload, output);
    }

    /**
     * Displays a grid containing all skills
     */
    private void initGrid() {
        JSONObject skillsArray = jsonFile.getSkillsArray();
        List<Question> dataBase = new ArrayList<>();
        Object[] questions = skillsArray.keySet().toArray();
        for (Object question : questions) {
            dataBase.add(new Question((String) question,true));
        }
        grid.setItems(dataBase);
        grid.setColumns("skill", "propertiesList");
    }

    private void initDialog() {
        setUpTemplate();

        addTemplate.addClickListener(e -> {
            skillTemplateEmpty();
            newTemplate.open();
            deleteQuestion = "";
        });

        submitTemplate.addClickListener(e -> {
            String requestString = request.getValue();
            String responseString = response.getValue();
            String[] slotArray = new String[]{slotOne.getValue(), slotTwo.getValue(), slotThree.getValue(), slotFour.getValue()};
            boolean error = requestString.isEmpty() || responseString.isEmpty();
            if (!error) {
                // delete skill whenever there is a skill to delete
                if (deleteQuestion.length() != 0){
                    jsonFile.deleteSkill(deleteQuestion);
                    System.out.println(jsonFile.deleteSkill(deleteQuestion));
                }

                // add new skill
                newQuestion = new Question(requestString,false);
                jsonFile.newSkill(newQuestion);
                List<String> properties = newQuestion.getPropertiesList();
                JSONObject actionConditions = new JSONObject();
                for (int i = 0; i < properties.size(); i++) {
                    actionConditions.put(properties.get(i), slotArray[i]);
                }
                jsonFile.addAction(newQuestion, responseString, actionConditions);

                skillTemplateEmpty();
                initGrid();
                newTemplate.close();
                grid.deselectAll();
            }
            else {
                if (requestString.isEmpty()){
                    request.setId("request-must-be-filled"); //TODO make it look better
                }else{
                    request.setId("request");
                }
                if (responseString.isEmpty()){
                    response.setId("response-must-be-filled"); //TODO make it look better
                }else{
                    response.setId("response");
                }
            }
        });

        cancelButton.addClickListener(e -> newTemplate.close());
    }

    /**
     * Set up the template for adding/editing a skill
     */
    private void setUpTemplate(){
        request.setWidth("500px");
        response.setWidth("500px");
        slotOne.setWidth("200px");
        slotTwo.setWidth("200px");
        slotThree.setWidth("200px");
        slotFour.setWidth("200px");
        slotTwo.setId("slot-two");
        slotFour.setId("slot-four");
        newTemplate.setWidth("550px");
        newTemplate.setHeight("600");
        newTemplate.add(request);
        newTemplate.add(slotOne);
        newTemplate.add(slotTwo);
        newTemplate.add(slotThree);
        newTemplate.add(slotFour);
        newTemplate.add(response);
        newTemplate.add(submitTemplate);
        newTemplate.add(cancelButton);
        request.setId("request-textfield");
        response.setId("response-textfield");
        addTemplate.setId("add-button");
        cancelButton.setId("cancel-button");
        submitTemplate.setId("submit-button");
    }

    private void initDeleteButton() {
        deleteEntry.setId("delete-button");
        deleteEntry.addClickListener(e -> {
            Object[] tempSet = grid.getSelectedItems().toArray();
            System.out.println(tempSet[0].toString());
            String deleteQuestion = tempSet[0].toString();
            jsonFile.deleteSkill(deleteQuestion);
            grid.deselectAll();
            initGrid();
        });
    }

    private void initEditButton() {
        editEntry.setId("edit-button");

        editEntry.addClickListener(e -> {
            if (grid.getSelectedItems().toArray().length != 0){ // if item is selected
                skillTemplateEmpty();
                newTemplate.open();
                Object [] tempSet = grid.getSelectedItems().toArray();
                String requestString = tempSet[0].toString();
                request.setValue(requestString);
                deleteQuestion = requestString;
                SkillParser skillParser = new SkillParser();
                response.setValue(skillParser.answer(requestString));

                Question question = new Question(requestString,false);
                List<String> slots = question.getPropertiesList();
                if (slots.size() >= 1){
                    slotOne.setValue(slots.get(0));
                }
                if (slots.size() >= 2){
                    slotTwo.setValue(slots.get(1));
                }
                if (slots.size() >= 3){
                    slotTwo.setValue(slots.get(2));
                }
                if (slots.size() >= 4){
                    slotTwo.setValue(slots.get(3));
                }
            }
        });
    }

    private void skillTemplateEmpty() {
        request.setValue("");
        response.setValue("");
        slotOne.setValue("");
        slotTwo.setValue("");
        slotThree.setValue("");
        slotFour.setValue("");

        request.setId("request-textfield");
        response.setId("response-textfield");
    }

    private void initUpload() {
        upload.setMaxFiles(1);
        upload.setDropLabel(new Label("Upload a max. 10KB file in .txt or .csv format"));
        upload.setAcceptedFileTypes("text/csv", ".txt");
        upload.setMaxFileSize(10000);
        upload.setUploadButton(mergeButton);
        upload.addFileRejectedListener(event -> {
            errorText.setText(event.getErrorMessage());
            output.add(errorText);
        });
        upload.addSucceededListener(event -> output.remove(errorText));
    }
}
