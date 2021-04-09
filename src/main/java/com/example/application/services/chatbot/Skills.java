package com.example.application.services.chatbot;

import com.example.application.services.utils.TextFileIO;

import java.util.ArrayList;
import java.util.List;

public class Skills {
    private List<Action> actions = new ArrayList<>();
    private String PATH = "src/main/java/com/example/application/services/chatbot/actions.txt";

    public void loadActions(){
        int id = 0;
        List<String> textFromFile = TextFileIO.read(PATH);
        for(String line : textFromFile){
            Action action = new Action();
            action.id=id++;
            String[] symbols = line.split(" ");
            int startIndex=1;
            if(CFG.isVariable(symbols[0])){
                action.variable=symbols[0];
            } else {
                startIndex=0;
                action.variable="<ACTION>";
            }
            StringBuilder expression = new StringBuilder();
            for(int i=startIndex; i<symbols.length; i++){
                    expression.append(symbols[i]).append(" ");
            }
            action.expression=expression.toString();
            actions.add(action);
        }
    }

    public void writeActions(){
        List<String> toBeWritten = new ArrayList<>();
        for(Action action : actions){
            toBeWritten.add(action.variable + " " + action.expression);
        }
        TextFileIO.write(PATH, toBeWritten);

    }

    public void addRule(Action action){
        action.id = actions.get(actions.size()-1).id +1;
        actions.add(action);
    }

    public void removeRule(int id){
        actions.removeIf(action -> action.id==id);
    }

    public List<Action> getActions() {
        return actions;
    }
}
