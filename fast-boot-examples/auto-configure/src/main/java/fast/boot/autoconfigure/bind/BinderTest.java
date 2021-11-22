package fast.boot.autoconfigure.bind;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "binder.test")
public class BinderTest {
    private String bname;

    private Integer bage;

    private BinderInnerTest binderInnerTest;


    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public Integer getBage() {
        return bage;
    }

    public void setBage(Integer bage) {
        this.bage = bage;
    }

    public BinderInnerTest getBinderInnerTest() {
        return binderInnerTest;
    }

    public void setBinderInnerTest(BinderInnerTest binderInnerTest) {
        this.binderInnerTest = binderInnerTest;
    }

    public static class BinderInnerTest{

        private String innerName;

        private Integer innerage;

        public String getInnerName() {
            return innerName;
        }

        public void setInnerName(String innerName) {
            this.innerName = innerName;
        }

        public Integer getInnerage() {
            return innerage;
        }

        public void setInnerage(Integer innerage) {
            this.innerage = innerage;
        }
    }
}
