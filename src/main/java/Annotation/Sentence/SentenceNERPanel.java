package Annotation.Sentence;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.ViewLayerType;
import AutoProcessor.Sentence.TurkishSentenceAutoNER;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import DataStructure.CounterHashMap;
import NamedEntityRecognition.NamedEntityType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentenceNERPanel extends SentenceAnnotatorPanel {
    private final HashMap<String, ArrayList<AnnotatedWord>> mappedWords;
    private final HashMap<String, ArrayList<AnnotatedSentence>> mappedSentences;
    private final TurkishSentenceAutoNER turkishSentenceAutoNER;

    /**
     * Constructor for the NER panel for an annotated sentence. Sets the attributes.
     * @param currentPath The absolute path of the annotated file.
     * @param fileName The raw file name of the annotated file.
     * @param mappedWords Enlists other annotated words that has the same word in the key.
     * @param mappedSentences Enlists other annotated sentence that contains the same word in the key.
     */
    public SentenceNERPanel(String currentPath, String fileName, HashMap<String, ArrayList<AnnotatedWord>> mappedWords, HashMap<String, ArrayList<AnnotatedSentence>> mappedSentences){
        super(currentPath, fileName, ViewLayerType.NER);
        this.mappedWords = mappedWords;
        this.mappedSentences = mappedSentences;
        list.setCellRenderer(new ListRenderer());
        turkishSentenceAutoNER = new TurkishSentenceAutoNER();
        setLayout(new BorderLayout());
    }

    /**
     * Updates the NER layer of the annotated word.
     */
    @Override
    protected void setWordLayer(){
        clickedWord.setNamedEntityType((String) list.getSelectedValue());
    }

    /**
     * Compares the size of the word and the size of the NER tag in pixels and returns the maximum of them.
     * @param word Word annotated.
     * @param g Graphics on which NER is drawn.
     * @return Maximum of the graphic sizes of word and its NER tag.
     */
    @Override
    protected int getMaxLayerLength(AnnotatedWord word, Graphics g){
        int maxSize = g.getFontMetrics().stringWidth(word.getName());
        if (word.getNamedEntityType() != null){
            int size = g.getFontMetrics().stringWidth(word.getNamedEntityType().toString());
            if (size > maxSize){
                maxSize = size;
            }
        }
        return maxSize;
    }

    /**
     * Draws the NER tag of the word.
     * @param word Annotated word itself.
     * @param g Graphics on which NER tag is drawn.
     * @param currentLeft Current position on the x-axis, where the NER tag will be aligned.
     * @param lineIndex Current line of the word, if the sentence resides in multiple lines on the screen.
     * @param wordIndex Index of the word in the annotated sentence.
     * @param maxSize Maximum size in pixels of anything drawn in the screen.
     * @param wordSize Array storing the sizes of all words in pixels in the annotated sentence.
     * @param wordTotal Array storing the total size until that word of all words in the annotated sentence.
     */
    @Override
    protected void drawLayer(AnnotatedWord word, Graphics g, int currentLeft, int lineIndex, int wordIndex, int maxSize, ArrayList<Integer> wordSize, ArrayList<Integer> wordTotal) {
        if (word.getNamedEntityType() != null){
            String correct = word.getNamedEntityType().toString();
            g.drawString(correct, currentLeft, (lineIndex + 1) * lineSpace + 30);
        }
    }

    /**
     * Sets the width and height of the JList that displays the NER tags.
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
     * Construct the tooltip text for every NER tag for selectedWord using the mappedSentences. The tooltip enlists
     * the example sentences that contains the selectedWord annotated with that tag. If the number of example sentence
     * are more than 20, it only displays the first 20 of them.
     */
    private class ListRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component cell = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            AnnotatedWord selectedWord = ((AnnotatedWord)sentence.getWord(selectedWordIndex));
            StringBuilder examples = new StringBuilder("<html>");
            int count = 0;
            if (mappedSentences.containsKey(selectedWord.getName())){
                for (AnnotatedSentence annotatedSentence : mappedSentences.get(selectedWord.getName())){
                    for (int i = 0; i < annotatedSentence.wordCount(); i++){
                        AnnotatedWord word = (AnnotatedWord) annotatedSentence.getWord(i);
                        if (word.getName().equals(selectedWord.getName()) && word.getNamedEntityType() != null){
                            if (word.getNamedEntityType().toString().equals(value)){
                                examples.append(annotatedSentence.toNamedEntityString(i)).append("<br>");
                                count++;
                            }
                        }
                    }
                    if (count >= 20){
                        break;
                    }
                }
            }
            examples.append("</html>");
            ((JComponent) cell).setToolTipText(examples.toString());
            return this;
        }
    }

    /**
     * Automatically detects the NER tag of words in the sentence using turkishSentenceAutoNER.
     */
    public void autoDetect(){
        turkishSentenceAutoNER.autoNER(sentence);
        sentence.save();
        this.repaint();
    }

    /**
     * Sorts the NER tags according to usage frequency (how many times they are used to tag that word) in decreasing
     * order.
     * @param word The selected word
     * @return NER tags sorted in decreasing order of usage frequency.
     */
    private NamedEntityType[] possibleValues(String word){
        if (!mappedWords.containsKey(word)){
            return NamedEntityType.values();
        }
        ArrayList<AnnotatedWord> words = mappedWords.get(word);
        CounterHashMap<NamedEntityType> counts = new CounterHashMap<>();
        for (NamedEntityType namedEntityType : NamedEntityType.values()){
            counts.put(namedEntityType);
        }
        for (AnnotatedWord annotatedWord : words){
            if (annotatedWord.getNamedEntityType() != null && !annotatedWord.getNamedEntityType().equals(NamedEntityType.NONE)){
                counts.put(annotatedWord.getNamedEntityType());
            }
        }
        List<Map.Entry<NamedEntityType, Integer>> sortedCounts = counts.topN(counts.size());
        NamedEntityType[] result = new NamedEntityType[sortedCounts.size()];
        int i = 0;
        for (Map.Entry<NamedEntityType, Integer> entry : sortedCounts){
            result[i] = entry.getKey();
            i++;
        }
        return result;
    }

    /**
     * Fills the JList that contains all possible NER tags. The NER tags are sorted in decreasing order of usage
     * frequency of those tags for that word.
     * @param sentence Sentence used to populate for the current word.
     * @param wordIndex Index of the selected word.
     * @return The index of the selected tag, -1 if nothing selected.
     */
    public int populateLeaf(AnnotatedSentence sentence, int wordIndex){
        int selectedIndex = -1;
        AnnotatedWord word = (AnnotatedWord) sentence.getWord(wordIndex);
        listModel.clear();
        NamedEntityType[] namedEntityTypeList = possibleValues(word.getName());
        for (int i = 0; i < namedEntityTypeList.length; i++){
            if (word.getNamedEntityType() != null && word.getNamedEntityType().equals(namedEntityTypeList[i])){
                selectedIndex = i;
            }
            listModel.addElement(namedEntityTypeList[i].toString());
        }
        return selectedIndex;
    }

}
