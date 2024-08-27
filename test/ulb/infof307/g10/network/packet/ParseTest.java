package ulb.infof307.g10.network.packet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParseTest {
    Parse parse = new Parse();

    @Test
    void testParse1D(){
        String content = "Shan;123";
        String[] res = parse.parse(content);
        assertEquals(2,res.length);
        assertEquals("Shan",res[0]);
        assertEquals("123",res[1]);
    }

    @Test
    void testParse2D(){
        String content = "Shan;123;samed;03;othmane;88";
        String[][] res = parse.parse(content,2);
        assertEquals(3,res.length);
        assertEquals("Shan",res[0][0]);
        assertEquals("123",res[0][1]);
        assertEquals("samed",res[1][0]);
        assertEquals("03",res[1][1]);
        assertEquals("othmane",res[2][0]);
        assertEquals("88",res[2][1]);
    }

    @Test
    void inverseParse1D(){
        String[] test = {"Shan","123"};
        String content = parse.inverseParse(test);
        assertEquals("Shan;123", content);
    }

    @Test
    void inverseParse2D(){
        String[][] test = {{"Shan","123"},{"samed","321"},{"JN","213"}};
        String content = parse.inverseParse(test);
        assertEquals("Shan;123;samed;321;JN;213;", content);
    }
}
