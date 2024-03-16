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

    protected int getStringSize(ParseNodeDrawable parseNode, Graphics g) {
        if (parseNode.numberOfChildren() == 0) {
            return g.getFontMetrics().stringWidth(parseNode.getLayerData(ViewLayerType.NER));
        } else {
            return g.getFontMetrics().stringWidth(parseNode.getData().getName());
        }
    }

    protected void drawString(ParseNodeDrawable parseNode, Graphics g, int x, int y){
        if (parseNode.numberOfChildren() == 0){
            g.drawString(parseNode.getLayerData(ViewLayerType.TURKISH_WORD), x, y);
            g.setColor(Color.RED);
            g.drawString(parseNode.getLayerData(ViewLayerType.NER), x, y + 20);
        } else {
            g.drawString(parseNode.getData().getName(), x, y);
        }
    }

    protected void setArea(ParseNodeDrawable parseNode, int x, int y, int stringSize){
        parseNode.setArea(x - 5, y - 15, stringSize + 10, 20);
    }

}
