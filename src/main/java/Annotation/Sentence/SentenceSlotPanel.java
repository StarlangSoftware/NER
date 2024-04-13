package Annotation.Sentence;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.ViewLayerType;
import AnnotatedSentence.AnnotatedWord;
import DataCollector.Sentence.SentenceAnnotatorPanel;

import java.awt.*;
import java.util.ArrayList;

public class SentenceSlotPanel extends SentenceAnnotatorPanel {
    private final ArrayList<String> entityList;

    /**
     * Constructor for the NER panel for an annotated sentence. Sets the attributes.
     * @param currentPath The absolute path of the annotated file.
     * @param fileName The raw file name of the annotated file.
     * @param entityList Possible list of entity tags that could be assigned to words.
     */
    public SentenceSlotPanel(String currentPath, String fileName, ArrayList<String> entityList){
        super(currentPath, fileName, ViewLayerType.SLOT);
        this.entityList = entityList;
        setLayout(new BorderLayout());
    }

    /**
     * Updates the Slot layer of the annotated word.
     */
    @Override
    protected void setWordLayer() {
        clickedWord.setSlot(list.getSelectedValue().toString());
    }

    /**
     * Draws the Slot tag of the word.
     * @param word Annotated word itself.
     * @param g Graphics on which Slot tag is drawn.
     * @param currentLeft Current position on the x-axis, where the Slot tag will be aligned.
     * @param lineIndex Current line of the word, if the sentence resides in multiple lines on the screen.
     * @param wordIndex Index of the word in the annotated sentence.
     * @param maxSize Maximum size in pixels of anything drawn in the screen.
     * @param wordSize Array storing the sizes of all words in pixels in the annotated sentence.
     * @param wordTotal Array storing the total size until that word of all words in the annotated sentence.
     */    @Override
    protected void drawLayer(AnnotatedWord word, Graphics g, int currentLeft, int lineIndex, int wordIndex, int maxSize, ArrayList<Integer> wordSize, ArrayList<Integer> wordTotal) {
        if (word.getSlot() != null){
            String correct = word.getSlot().toString();
            g.drawString(correct, currentLeft, (lineIndex + 1) * lineSpace + 30);
        }
    }

    /**
     * Compares the size of the word and the size of the Slot tag in pixels and returns the maximum of them.
     * @param word Word annotated.
     * @param g Graphics on which Slot is drawn.
     * @return Maximum of the graphic sizes of word and its Slot tag.
     */
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

    /**
     * Sets the width and height of the JList that displays the Slot tags.
     */
    @Override
    protected void setBounds() {
        pane.setBounds(((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getX(), ((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getY() + 20, 240, (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.4));
    }

    /**
     * Sets the space between displayed lines in the sentence.
     */
    @Override
    protected void setLineSpace() {
        lineSpace = 80;
    }

    /**
     * Fills the JList that contains all possible Slot tags.
     * @param sentence Sentence used to populate for the current word.
     * @param wordIndex Index of the selected word.
     * @return The index of the selected tag, -1 if nothing selected.
     */
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
