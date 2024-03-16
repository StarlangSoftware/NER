package Annotation.Sentence;

import AnnotatedSentence.AnnotatedCorpus;
import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import DataCollector.ParseTree.TreeEditorPanel;
import DataCollector.Sentence.SentenceAnnotatorFrame;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import NamedEntityRecognition.NamedEntityType;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SentenceNERFrame extends SentenceAnnotatorFrame {

    private final HashMap<String, ArrayList<AnnotatedWord>> mappedWords = new HashMap<>();
    private final HashMap<String, ArrayList<AnnotatedSentence>> mappedSentences = new HashMap<>();
    private final JCheckBox autoNERDetectionOption;

    public SentenceNERFrame(){
        super();
        AnnotatedCorpus annotatedCorpus;
        annotatedCorpus = new AnnotatedCorpus(new File(TreeEditorPanel.phrasePath));
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

    public void next(int count){
        super.next(count);
        SentenceNERPanel current;
        current = (SentenceNERPanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoNERDetectionOption.isSelected()){
            current.autoDetect();
        }
    }

    public void previous(int count){
        super.previous(count);
        SentenceNERPanel current;
        current = (SentenceNERPanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoNERDetectionOption.isSelected()){
            current.autoDetect();
        }
    }

}
