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
    private HashMap<String, ArrayList<AnnotatedWord>> mappedWords;
    private HashMap<String, ArrayList<AnnotatedSentence>> mappedSentences;
    private TurkishSentenceAutoNER turkishSentenceAutoNER;

    public SentenceNERPanel(String currentPath, String fileName, HashMap<String, ArrayList<AnnotatedWord>> mappedWords, HashMap<String, ArrayList<AnnotatedSentence>> mappedSentences){
        super(currentPath, fileName, ViewLayerType.NER);
        this.mappedWords = mappedWords;
        this.mappedSentences = mappedSentences;
        list.setCellRenderer(new ListRenderer());
        turkishSentenceAutoNER = new TurkishSentenceAutoNER();
        setLayout(new BorderLayout());
    }

    private class ListRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component cell = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            AnnotatedWord selectedWord = ((AnnotatedWord)sentence.getWord(selectedWordIndex));
            String examples = "<html>";
            int count = 0;
            if (mappedSentences.containsKey(selectedWord.getName())){
                for (AnnotatedSentence annotatedSentence : mappedSentences.get(selectedWord.getName())){
                    for (int i = 0; i < annotatedSentence.wordCount(); i++){
                        AnnotatedWord word = (AnnotatedWord) annotatedSentence.getWord(i);
                        if (word.getName().equals(selectedWord.getName()) && word.getNamedEntityType() != null){
                            if (word.getNamedEntityType().toString().equals(value)){
                                examples += annotatedSentence.toNamedEntityString(i) + "<br>";
                                count++;
                            }
                        }
                    }
                    if (count >= 20){
                        break;
                    }
                }
            }
            examples += "</html>";
            ((JComponent) cell).setToolTipText(examples);
            return this;
        }
    }

    public void autoDetect(){
        turkishSentenceAutoNER.autoNER(sentence);
        sentence.save();
        this.repaint();
    }

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
