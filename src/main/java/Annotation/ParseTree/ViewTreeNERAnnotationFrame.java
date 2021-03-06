package Annotation.ParseTree;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.*;
import AnnotatedTree.Processor.Condition.IsTurkishLeafNode;
import AnnotatedTree.Processor.NodeDrawableCollector;
import DataCollector.ParseTree.TreeEditorPanel;
import DataCollector.ParseTree.ViewTreeAnnotationFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ViewTreeNERAnnotationFrame extends ViewTreeAnnotationFrame {

    protected void updateData(int row, String newValue){
        data.get(row).set(TAG_INDEX, newValue);
        ParseTreeDrawable parseTree = treeBank.get(Integer.parseInt(data.get(row).get(COLOR_COLUMN_INDEX - 1)));
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        ParseNodeDrawable parseNode = leafList.get(Integer.parseInt(data.get(row).get(WORD_POS_INDEX)) - 1);
        parseNode.getLayerInfo().setLayerData(ViewLayerType.NER, newValue);
        parseTree.save();
    }

    protected void prepareData(TreeBankDrawable treeBank) {
        data = new ArrayList<>();
        for (int i = 0; i < treeBank.size(); i++){
            ParseTreeDrawable parseTree = treeBank.get(i);
            NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
            for (int j = 0; j < leafList.size(); j++){
                LayerInfo layerInfo = leafList.get(j).getLayerInfo();
                ArrayList<String> row = new ArrayList<>();
                row.add(parseTree.getFileDescription().getRawFileName());
                row.add("" + (j + 1));
                try {
                    row.add(layerInfo.getTurkishWordAt(0));
                } catch (LayerNotExistsException | WordNotExistsException e) {
                    row.add("-");
                }
                if (layerInfo.getLayerData(ViewLayerType.NER) != null){
                    row.add(layerInfo.getLayerData(ViewLayerType.NER));
                } else {
                    row.add("-");
                }
                row.add(((ParseNodeDrawable) parseTree.getRoot()).toTurkishSentence());
                row.add("" + i);
                row.add("0");
                data.add(row);
            }
        }
    }

    public ViewTreeNERAnnotationFrame(TreeBankDrawable treeBank, TreeNERFrame treeNERFrame){
        super(treeBank, "Named Entity");
        prepareData(treeBank);
        dataTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2){
                    int row = dataTable.rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        String fileName = data.get(row).get(0);
                        treeNERFrame.addPanelToFrame(new TreeNERPanel(TreeEditorPanel.treePath, fileName, false), fileName);
                    }
                }
            }
        });
        JScrollPane tablePane = new JScrollPane(dataTable);
        add(tablePane, BorderLayout.CENTER);
    }
}
