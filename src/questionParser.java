import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.international.pennchinese.ChineseGrammaticalStructure;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

/**
 * Created by diaosuyi on 12/23/16.
 */
public class questionParser {
    private LexicalizedParser lp;
    //next is the final output of this Parser;
    private String next;
    //question is the input of this Parser;
    private String input;
    private typeDetector.TYPE type;

    questionParser(String input1){
        type = typeDetector.TYPE.UNDECIDED;
        input = input1;
        String modelpath="lib/models/chinesePCFG.ser.gz";
        lp = LexicalizedParser.loadModel(modelpath);
        process(input);
    }

    public String getParsedQuestion(){
        return next;
    }

    public typeDetector.TYPE getType(){
        return type;
    }

    private String process(String str) {
        try{
            String[] attr = str.split("\t");
            if(attr.length < 2) return null;
            str = attr[0];

            wordSpliter ws = new wordSpliter(str);
            String tokenizedSentence = ws.output();

            Tree t = lp.parse(tokenizedSentence);
            ChineseGrammaticalStructure gs = new ChineseGrammaticalStructure(t);
            Collection<TypedDependency> tdl = gs.typedDependenciesCollapsedTree();

            if(tdl.size()<1) return null;

            boolean isdet = false;
            for(TypedDependency dep:tdl){
                if(dep.reln().toString().equals("det")){

                    type = typeDetector.TYPE.COMPLEX_CORED;

                    String word = dep.dep().word();
                    if(!( word.matches("哪|什么|谁") )) continue;
                    isdet = true;
                    String re = dep.dep().word()+".*"+dep.gov().word();
                    String ans = str.replaceAll(re, attr[1]);
                    ans = ans.replaceAll("[\\?？]", "。");

                    wordSpliter wss = new wordSpliter(ans);
                    String s = wss.output();

                    Tree t1 = lp.parse(s);
                    ChineseGrammaticalStructure gs1 = new ChineseGrammaticalStructure(t1);
                    Collection<TypedDependency> anstdl = gs1.typedDependenciesCollapsedTree();

                    next = ans;
                }
            }
            boolean ispn = false;
            if(!isdet){
                TypedDependency dep = tdl.toArray(new TypedDependency[0])[tdl.size()-1];
                if(dep.dep().tag().equals("PN") || dep.dep().word().equals("是")){

                    type = typeDetector.TYPE.COMPLEX_DISCORED;

                    ispn = true;
                    String re = dep.dep().word();

                    String ans = str.replaceAll(re, (dep.dep().word().equals("是")?"是":"")+attr[1]);
                    ans = ans.replaceAll("[\\?？]", "");
                    String word = (dep.dep().word().equals("是")?"是":"PN");

                    wordSpliter wss = new wordSpliter(ans);
                    String s = wss.output();

                    Tree t1 = lp.parse(s);
                    ChineseGrammaticalStructure gs1 = new ChineseGrammaticalStructure(t1);
                    Collection<TypedDependency> anstdl = gs1.typedDependenciesCollapsedTree();

                    next = ans;
                }
            }

        } finally {
            return next;
        }
    }
}
