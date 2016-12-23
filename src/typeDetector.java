/**
 * Created by diaosuyi on 12/23/16.
 */
public class typeDetector {
    private String question;
    private String type;

    typeDetector(String input)
    {
        question = input;
        type = "";
        answerTypeDetect();
    }


    private String[][] typeDetectora = new String[][]{
            {"order", "第几"},
            {"sentence", "下一句"},
            {"person", "谁", "哪位", "什么名字"},
            {"dynasty", "哪个朝", "朝代是", "王朝是", "哪个王朝"},
            {"city","哪个省","哪省","哪个城市","区是","哪市","那个城市","哪座城市","什么州","什么市","哪个州","哪个市","市是","省份是","哪一座城市","省份是","县份是","哪一个省份","哪一省份","哪一省","那一省份","那一省","哪一区","哪一个区","哪一市","什么区","什么省"},
            {"country", "哪个国家", "哪国", "国籍是", "的国籍", "国家是"},
            {"continent", "哪块大陆", "哪个大陆", "哪一个大陆", "哪一块大陆", "大陆是", "大洲是", "哪个大洲", "哪一个大洲"},
            {"place", "哪儿", "哪里", "什么地方", "地方是", "哪个地方", "何处", "何地", "地点是"},
            {"date", "哪个时", "什么时候", "哪一月", "哪一天", "几月", "几日", "几号", "几点", "几时", "何时", "何日", "哪月", "哪天"},
            {"years", "公元多少", "公元前多少", "哪一年", "第几年", "年份是", "哪年", "何年"},
            {"number", "几", "多少", "有多少", "多远", "多重", "多大", "多小", "多宽", "多热", "多快", "多少","多久","多长", "海拔是", "高度是", "长度是", "距离是", "的海拔", "的长度", "的距离"},
            {"noun", "叫什么","哪", "什么","怎"},

    };

    void answerTypeDetect() {
        for (String[] one : typeDetectora)
            if (match(question, one)) {
                type = one[0];
                //System.out.println(answerType);
                return;
            }
    }

    public String getType()
    {
        return type;
    }

    boolean match(String word, String[] matchWords) {
        for (String w : matchWords)
            if (word.contains(w))
                return true;
        return false;
    }

    public static void main(String[] args){
        typeDetector a = new typeDetector("谁是周杰伦");
        System.out.print(a.getType());
    }
}
