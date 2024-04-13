package Annotation.ParseTree;

import AnnotatedTree.TreeBankDrawable;
import AutoProcessor.ParseTree.TreeAutoNER;
import AutoProcessor.ParseTree.TurkishTreeAutoNER;
import DataCollector.ParseTree.TreeEditorFrame;
import DataCollector.ParseTree.TreeEditorPanel;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class TreeNERFrame extends TreeEditorFrame {
    private final JCheckBox autoNEROption;

    /**
     * Constructor of the NER frame for parse trees. It reads the annotated tree bank and adds automatic NER detection
     * button.
     */
    public TreeNERFrame(){
        this.setTitle("Named Entity Recognition Editor");
        autoNEROption = new JCheckBox("AutoNER", true);
        toolBar.add(autoNEROption);
        TreeBankDrawable treeBank = new TreeBankDrawable(new File(TreeEditorPanel.treePath));
        JMenuItem itemViewAnnotations = addMenuItem(projectMenu, "View Annotations", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        itemViewAnnotations.addActionListener(e -> new ViewTreeNERAnnotationFrame(treeBank, this));
    }

    @Override
    protected TreeEditorPanel generatePanel(String currentPath, String rawFileName) {
        return new TreeNERPanel(currentPath, rawFileName, true);
    }

    /**
     * The method automatically assigns NER tags to words in the parse tree using TurkishTreeAutoNER.
     */
    private void autoNER(){
        TreeAutoNER treeAutoNER;
        if (autoNEROption.isSelected()){
            TreeEditorPanel current = (TreeEditorPanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
            treeAutoNER = new TurkishTreeAutoNER();
            treeAutoNER.autoNER(current.currentTree);
            current.currentTree.reload();
            current.repaint();
        }
    }

    /**
     * The function displays the next tree according to count and the index of the parse tree. For example, if the
     * current tree  fileName is 0123.train, after the call of nextTree(3), ViewerPanel will display 0126.train. If the
     * next tree  does not exist, nothing will happen. If the autoNEROption is selected, it automatically
     * assigns NER tags to words.
     * @param count Number of trees to go forward
     */
    protected void nextTree(int count){
        super.nextTree(count);
        autoNER();
    }

    /**
     * Overloaded function that displays the previous tree according to count and the index of the parse tree. For
     * example, if the current tree fileName is 0123.train, after the call of previousTree(4), ViewerPanel will
     * display 0119.train. If the previous tree does not exist, nothing will happen. If the autoNEROption is selected,
     * it automatically  assigns NER tags to words.
     * @param count Number of trees to go backward
     */
    protected void previousTree(int count){
        super.previousTree(count);
        autoNER();
    }


}
