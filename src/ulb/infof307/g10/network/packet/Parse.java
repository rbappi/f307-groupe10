package ulb.infof307.g10.network.packet;


import java.util.Arrays;

/**
 * The Parse class manage the transformation of an information into message and a message into an information
 * The form of message is "Jean;12345"
 * The form of information is [Jean,12345]
 */
public class Parse {

    public Parse(){}
    /**
     * This function is used to split the received message and to transform it into a list of information
     * Example: String content = "Jean;12345"
     *          Return List = [Jean,12345]
     * @param content is a str message of data
     * @return String[] parsed content
     */
    public String[] parse(String content) { return content.split(";"); }


    public String[][] parse(String content, int n) {
        String[] tokens = content.split(";");
        int numArrays = (int) Math.ceil((double) tokens.length / n);
        String[][] result = new String[numArrays][];
        for (int i = 0; i < numArrays; i++) {
            int start = i * n;
            int end = Math.min(start + n, tokens.length);
            String[] arr = Arrays.copyOfRange(tokens, start, end);
            result[i] = arr;
        }
        return result;
    }
    /**
     * This function is used to split a list of information and to transform it into a message
     * Example: String List = [Jean,12345]
     *          Return content = "Jean;12345"
     * @param vector  is a list of data
     * @return String inverted parsed content
     */
    public String inverseParse(String[] vector){ return String.join(";", vector); }

    public String inverseParse(String[][] vector) {
        StringBuilder sb = new StringBuilder();
        for (String[] arr : vector) {
            sb.append(String.join(";", arr)).append(";");
        }
        return sb.toString();
    }
}