package Annotation.Sentence;

import DataCollector.Sentence.SentenceAnnotatorFrame;
import DataCollector.Sentence.SentenceAnnotatorPanel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SentenceSlotFrame extends SentenceAnnotatorFrame {
    private final ArrayList<String> entityList = new ArrayList<>();

    /**
     * Constructor of the Slot frame for annotated sentence. Reads possible entity tags from entities.txt and assigns
     * them to entityList attribute.
     */
    public SentenceSlotFrame(){
        super();
        try {
            Scanner input = new Scanner(new File("entities.txt"));
            while (input.hasNext()){
                entityList.add(input.next());
            }
            input.close();
        } catch (FileNotFoundException ignored) {
        }
    }

    @Override
    protected SentenceAnnotatorPanel generatePanel(String currentPath, String rawFileName) {
        return new SentenceSlotPanel(currentPath, rawFileName, entityList);
    }
}
