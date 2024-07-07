package AutoProcessor.ParseTree;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.Condition.IsTurkishLeafNode;
import AnnotatedTree.Processor.NodeDrawableCollector;
import Dictionary.Word;

import java.util.ArrayList;
import java.util.Locale;

public class TurkishTreeAutoNER extends TreeAutoNER {

    public TurkishTreeAutoNER(){
        super(ViewLayerType.TURKISH_WORD);
    }

    /**
     * The method assigns the words "bay" and "bayan" PERSON tag. The method also checks the PERSON gazetteer, and if
     * the word exists in the gazetteer, it assigns PERSON tag. The parent node should have the proper noun tag NNP.
     * @param parseTree The tree for which PERSON named entities checked.
     */
    protected void autoDetectPerson(ParseTreeDrawable parseTree) {
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            if (!parseNode.layerExists(ViewLayerType.NER)){
                String word = parseNode.getLayerData(ViewLayerType.TURKISH_WORD).toLowerCase(new Locale("tr"));
                if (Word.isHonorific(word) && parseNode.getParent().getData().getName().equals("NNP")){
                    parseNode.getLayerInfo().setLayerData(ViewLayerType.NER, "PERSON");
                }
                parseNode.checkGazetteer(personGazetteer, word);
            }
        }
    }

    /**
     * The method checks the LOCATION gazetteer, and if the word exists in the gazetteer, it assigns the LOCATION tag.
     * @param parseTree The tree for which LOCATION named entities checked.
     */
    protected void autoDetectLocation(ParseTreeDrawable parseTree) {
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            if (!parseNode.layerExists(ViewLayerType.NER)){
                String word = parseNode.getLayerData(ViewLayerType.TURKISH_WORD).toLowerCase(new Locale("tr"));
                parseNode.checkGazetteer(locationGazetteer, word);
            }
        }
    }

    /**
     * The method assigns the words "corp.", "inc.", and "co" ORGANIZATION tag. The method also checks the
     * ORGANIZATION gazetteer, and if the word exists in the gazetteer, it assigns ORGANIZATION tag.
     * @param parseTree The tree for which ORGANIZATION named entities checked.
     */
    protected void autoDetectOrganization(ParseTreeDrawable parseTree) {
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            if (!parseNode.layerExists(ViewLayerType.NER)){
                String word = parseNode.getLayerData(ViewLayerType.TURKISH_WORD).toLowerCase(new Locale("tr"));
                if (Word.isOrganization(word)){
                    parseNode.getLayerInfo().setLayerData(ViewLayerType.NER, "ORGANIZATION");
                }
                parseNode.checkGazetteer(organizationGazetteer, word);
            }
        }
    }

    /**
     * The method checks for the MONEY entities using regular expressions. After that, if the expression is a MONEY
     * expression, it also assigns the previous nodes, which may included numbers or some monetarial texts, MONEY tag.
     * @param parseTree The tree for which MONEY named entities checked.
     */
    protected void autoDetectMoney(ParseTreeDrawable parseTree) {
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (int i = 0; i < leafList.size(); i++) {
            ParseNodeDrawable parseNode = leafList.get(i);
            if (!parseNode.layerExists(ViewLayerType.NER)){
                String word = parseNode.getLayerData(ViewLayerType.TURKISH_WORD).toLowerCase(new Locale("tr"));
                if (Word.isMoney(word)) {
                    parseNode.getLayerInfo().setLayerData(ViewLayerType.NER, "MONEY");
                    int j = i - 1;
                    while (j >= 0){
                        ParseNodeDrawable previous = leafList.get(j);
                        if (previous.getParent().getData().getName().equals("CD")){
                            previous.getLayerInfo().setLayerData(ViewLayerType.NER, "MONEY");
                        } else {
                            break;
                        }
                        j--;
                    }
                }
            }
        }
    }

    /**
     * The method checks for the TIME entities using regular expressions. After that, if the expression is a TIME
     * expression, it also assigns the previous texts, which are numbers, TIME tag.
     * @param parseTree The tree for which TIME named entities checked.
     */
    protected void autoDetectTime(ParseTreeDrawable parseTree) {
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (int i = 0; i < leafList.size(); i++){
            ParseNodeDrawable parseNode = leafList.get(i);
            if (!parseNode.layerExists(ViewLayerType.NER)){
                String word = parseNode.getLayerData(ViewLayerType.TURKISH_WORD).toLowerCase(new Locale("tr"));
                if (Word.isTime(word)){
                    parseNode.getLayerInfo().setLayerData(ViewLayerType.NER, "TIME");
                    if (i > 0){
                        ParseNodeDrawable previous = leafList.get(i - 1);
                        if (previous.getParent().getData().getName().equals("CD")){
                            previous.getLayerInfo().setLayerData(ViewLayerType.NER, "TIME");
                        }
                    }
                }
            }
        }
    }
}
