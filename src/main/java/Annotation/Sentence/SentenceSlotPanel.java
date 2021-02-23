package Annotation.Sentence;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.ViewLayerType;
import AnnotatedSentence.AnnotatedWord;
import DataCollector.Sentence.SentenceAnnotatorPanel;

import java.awt.*;
import java.util.ArrayList;

public class SentenceSlotPanel extends SentenceAnnotatorPanel {
    private ArrayList<String> entityList;

    public SentenceSlotPanel(String currentPath, String fileName, ArrayList<String> entityList){
        super(currentPath, fileName, ViewLayerType.SLOT);
        this.entityList = entityList;
        setLayout(new BorderLayout());
    }

    public int populateLeaf(AnnotatedSentence sentence, int wordIndex){
        int selectedIndex = -1;
        AnnotatedWord word = (AnnotatedWord) sentence.getWord(wordIndex);
        listModel.clear();
        for (int i = 0; i < entityList.size(); i++){
            if (word.getSlot() != null && word.getSlot().toString().equals(entityList.get(i))){
                selectedIndex = i;
            }
            listModel.addElement(entityList.get(i));
        }
        return selectedIndex;
    }

}
