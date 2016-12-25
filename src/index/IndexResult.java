package index;

/**
 * Created by RTCarl on 2016/12/24.
 */
public class IndexResult {

    private String title;
    private Integer no;
    private String contents;


    public IndexResult(String title, Integer no, String contents) {
        this.title = title;
        this.no = no;
        this.contents = contents;
    }

    public IndexResult(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //public Integer getNo() {
        //return no;
    //}

    //public void setNo(Integer no) {
        //this.no = no;
    //}

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
