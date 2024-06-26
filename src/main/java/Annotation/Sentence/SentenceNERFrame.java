package Annotation.Sentence;

import AnnotatedSentence.AnnotatedCorpus;
import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import DataCollector.Sentence.SentenceAnnotatorFrame;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import NamedEntityRecognition.NamedEntityType;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class SentenceNERFrame extends SentenceAnnotatorFrame {

    private final HashMap<String, ArrayList<AnnotatedWord>> mappedWords = new HashMap<>();
    private final HashMap<String, ArrayList<AnnotatedSentence>> mappedSentences = new HashMap<>();
    private final JCheckBox autoNERDetectionOption;

    /**
     * Constructor of the NER frame for annotated sentence. It reads the annotated sentence corpus and adds automatic
     * NER detection button. It also creates mappedWords and mappedSentences. mappedWords will be used to show the
     * user how many times that word was annotated with different NER tags. mappedSentences will be used to show the
     * user how other sentences with that word was annotated.
     */
    public SentenceNERFrame(){
        super();
        AnnotatedCorpus annotatedCorpus;
        String subFolder = "false";
        Properties properties1 = new Properties();
        try {
            properties1.load(Files.newInputStream(new File("config.properties").toPath()));
            if (properties1.containsKey("subFolder")){
                subFolder = properties1.getProperty("subFolder");
            }
        } catch (IOException ignored) {
        }
        annotatedCorpus = readCorpus(subFolder);
        for (int i = 0; i < annotatedCorpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) annotatedCorpus.getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getName() != null && word.getNamedEntityType() != null && !word.getNamedEntityType().equals(NamedEntityType.NONE)){
                    ArrayList<AnnotatedWord> annotatedWords;
                    if (mappedWords.containsKey(word.getName())){
                        annotatedWords = mappedWords.get(word.getName());
                    } else {
                        annotatedWords = new ArrayList<>();
                    }
                    annotatedWords.add(word);
                    mappedWords.put(word.getName(), annotatedWords);
                    ArrayList<AnnotatedSentence> annotatedSentences;
                    if (mappedSentences.containsKey(word.getName())){
                        annotatedSentences = mappedSentences.get(word.getName());
                    } else {
                        annotatedSentences = new ArrayList<>();
                    }
                    annotatedSentences.add(sentence);
                    mappedSentences.put(word.getName(), annotatedSentences);
                }
            }
        }
        autoNERDetectionOption = new JCheckBox("Auto Named Entity Recognition", false);
        toolBar.add(autoNERDetectionOption);
        JMenuItem itemViewAnnotated = addMenuItem(projectMenu, "View Annotations", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        itemViewAnnotated.addActionListener(e -> new ViewSentenceNERAnnotationFrame(annotatedCorpus, this));
        JOptionPane.showMessageDialog(this, "Annotated corpus is loaded!", "Named Entity Annotation", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected SentenceAnnotatorPanel generatePanel(String currentPath, String rawFileName) {
        return new SentenceNERPanel(currentPath, rawFileName, mappedWords, mappedSentences);
    }

    /**
     * The next method takes an int count as input and moves forward along the SentenceNERPanels as much as the
     * count. If the autoNERDetectionOption is selected, it automatically assigns NER tags to words.
     * @param count Integer count is used to move forward.
     */
    public void next(int count){
        super.next(count);
        SentenceNERPanel current;
        current = (SentenceNERPanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoNERDetectionOption.isSelected()){
            current.autoDetect();
        }
    }

    /**
     * The previous method takes an int count as input and moves backward along the SentenceNERPanels as much as the
     * count. If the autoNERDetectionOption is selected, it automatically assigns NER tags to words.
     * @param count Integer count is used to move backward.
     */
    public void previous(int count){
        super.previous(count);
        SentenceNERPanel current;
        current = (SentenceNERPanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoNERDetectionOption.isSelected()){
            current.autoDetect();
        }
    }

}
