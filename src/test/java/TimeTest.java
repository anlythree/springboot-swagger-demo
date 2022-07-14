import org.junit.Test;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeTest {

    @Test
    public void test1(){
        LocalDateTime now = LocalDateTime.now();
        now.atZone(ZoneId.of("HKT"));
    }

    @Test
    public void test2(){
        String a = "EST\n" +
                "HST\n" +
                "MST\n" +
                "ACT\n" +
                "AET\n" +
                "AGT\n" +
                "ART\n" +
                "AST\n" +
                "BET\n" +
                "BST\n" +
                "CAT\n" +
                "CNT\n" +
                "CST\n" +
                "CTT\n" +
                "EAT\n" +
                "ECT\n" +
                "IET\n" +
                "IST\n" +
                "JST\n" +
                "MIT\n" +
                "NET\n" +
                "NST\n" +
                "PLT\n" +
                "PNT\n" +
                "PRT\n" +
                "PST\n" +
                "SST\n" +
                "VST";
        System.out.println(StringUtils.replace(a,"\n",","));
    }
}