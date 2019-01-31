package li.allan.easycache.remote.serializer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author lialun
 */
public class DemoBean implements Serializable {
    private int i;
    private String str;
    private SubClass subClass;

    public DemoBean() {
    }

    public DemoBean(int i, String str, SubClass subClass) {
        this.i = i;
        this.str = str;
        this.subClass = subClass;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public SubClass getSubClass() {
        return subClass;
    }

    public void setSubClass(SubClass subClass) {
        this.subClass = subClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DemoBean demoBean = (DemoBean) o;
        return i == demoBean.i &&
                Objects.equals(str, demoBean.str) &&
                Objects.equals(subClass, demoBean.subClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, str, subClass);
    }
}

class SubClass implements Serializable {
    private String[] sub;
    private double d;

    public SubClass() {
    }

    public SubClass(String[] sub, double d) {
        this.sub = sub;
        this.d = d;
    }

    public String[] getSub() {
        return sub;
    }

    public void setSub(String[] sub) {
        this.sub = sub;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubClass subClass = (SubClass) o;
        return Double.compare(subClass.d, d) == 0 &&
                Arrays.equals(sub, subClass.sub);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(d);
        result = 31 * result + Arrays.hashCode(sub);
        return result;
    }
}
