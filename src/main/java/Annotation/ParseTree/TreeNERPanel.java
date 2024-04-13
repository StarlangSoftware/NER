package Annotation.ParseTree;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.*;
import DataCollector.ParseTree.TreeAction.LayerAction;
import DataCollector.ParseTree.TreeLeafEditorPanel;
import NamedEntityRecognition.NamedEntityType;

import javax.swing.*;
import java.awt.*;

public class TreeNERPanel extends TreeLeafEditorPanel {
    private final JList list;
    private final DefaultListModel listModel;

    /**
     * Constructor for the NER panel for a parse tree. Constructs the list used to annotated words. It also adds the
     * list selection listener which will update the parse tree according to the selection.
     * @param path The absolute path of the annotated parse tree.
     * @param fileName The raw file name of the annotated parse tree.
     * @param defaultFillEnabled If true, automatic annotation will be done.
     */
    public TreeNERPanel(String path, String fileName, boolean defaultFillEnabled) {
        super(path, fileName, ViewLayerType.NER, defaultFillEnabled);
        listModel = new DefaultListModel();
        list = new JList(listModel);
        list.setVisible(false);
        list.addListSelectionListener(listSelectionEvent -> {
            if (!listSelectionEvent.getValueIsAdjusting()) {
                if (list.getSelectedIndex() != -1 && previousNode != null) {
                    previousNode.setSelected(false);
                    LayerAction action = new LayerAction(((TreeNERPanel)((JList) listSelectionEvent.getSource()).getParent().getParent().getParent()), previousNode.getLayerInfo(), NamedEntityType.values()[list.getSelectedIndex()].toString(), ViewLayerType.NER);
                    actionList.add(action);
                    action.execute();
                    list.setVisible(false);
                    pane.setVisible(false);
                    isEditing = false;
                    repaint();
                }
            }
        });
        list.setFocusTraversalKeysEnabled(false);
        pane = new JScrollPane(list);
        add(pane);
        pane.setFocusTraversalKeysEnabled(false);
        setFocusable(false);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Fills the JList that contains all possible NER tags.
     * @param node Selected node for which options will be displayed.
     */
    public void populateLeaf(ParseNodeDrawable node){
        int selectedIndex = -1;
        if (previousNode != null){
            previousNode.setSelected(false);
        }
        previousNode = node;
        listModel.clear();
        for (int i = 0; i < NamedEntityType.values().length; i++){
            if (node.getLayerData(ViewLayerType.NER) != null && node.getLayerData(ViewLayerType.NER).equals(NamedEntityType.values()[i].toString())){
                selectedIndex = i;
            }
            listModel.addElement(NamedEntityType.values()[i].toString());
        }
        if (selectedIndex != -1){
            list.setValueIsAdjusting(true);
            list.setSelectedIndex(selectedIndex);
        }
        list.setVisible(true);
        pane.setVisible(true);
        pane.getVerticalScrollBar().setValue(0);
        pane.setBounds(node.getArea().getX() - 5, node.getArea().getY() + 30, 200, 90);
        this.repaint();
        isEditing = true;
    }

    /**
     * The size of the string displayed. If it is a leaf node, it returns the size of the NER tag. If it is a non-leaf
     * node, it returns the size of the symbol in the node.
     * @param parseNode Parse node
     * @param g Graphics on which tree will be drawn.
     * @return Size of the string displayed.
     */
    protected int getStringSize(ParseNodeDrawable parseNode, Graphics g) {
        if (parseNode.numberOfChildren() == 0) {
            return g.getFontMetrics().stringWidth(parseNode.getLayerData(ViewLayerType.NER));
        } else {
            return g.getFontMetrics().stringWidth(parseNode.getData().getName());
        }
    }

    /**
     * Draws the NER tag of the word in the parse node.
     * @param parseNode Parse Node
     * @param g Graphics on which symbol is drawn.
     * @param x x coordinate
     * @param y y coordinate
     */
    protected void drawString(ParseNodeDrawable parseNode, Graphics g, int x, int y){
        if (parseNode.numberOfChildren() == 0){
            g.drawString(parseNode.getLayerData(ViewLayerType.TURKISH_WORD), x, y);
            g.setColor(Color.RED);
            g.drawString(parseNode.getLayerData(ViewLayerType.NER), x, y + 20);
        } else {
            g.drawString(parseNode.getData().getName(), x, y);
        }
    }

    /**
     * Sets the size of the enclosing area of the parse node (for selecting, editing etc.).
     * @param parseNode Parse Node
     * @param x x coordinate of the center of the node.
     * @param y y coordinate of the center of the node.
     * @param stringSize Size of the string in terms of pixels.
     */
    protected void setArea(ParseNodeDrawable parseNode, int x, int y, int stringSize){
        parseNode.setArea(x - 5, y - 15, stringSize + 10, 20);
    }

}
