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

    @Override
    protected void setWordLayer() {
        clickedWord.setSlot(list.getSelectedValue().toString());
    }

    @Override
    protected void drawLayer(AnnotatedWord word, Graphics g, int currentLeft, int lineIndex, int wordIndex, int maxSize, ArrayList<Integer> wordSize, ArrayList<Integer> wordTotal) {
        if (word.getSlot() != null){
            String correct = word.getSlot().toString();
            g.drawString(correct, currentLeft, (lineIndex + 1) * lineSpace + 30);
        }
    }

    @Override
    protected int getMaxLayerLength(AnnotatedWord word, Graphics g) {
        int maxSize = g.getFontMetrics().stringWidth(word.getName());
        if (word.getSlot() != null){
            int size = g.getFontMetrics().stringWidth(word.getSlot().toString());
            if (size > maxSize){
                maxSize = size;
            }
        }
        return maxSize;
    }

    @Override
    protected void setBounds() {
        pane.setBounds(((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().x, ((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().y + 20, 240, (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.4));
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
