package peter.azzie;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

class LineParser {
    public static String encodeString(String str){
        boolean wrapInQuotes = false;
        StringBuilder output = new StringBuilder();
        for (char c : str.toCharArray()){
            switch (c){
                case ' ':
                    wrapInQuotes = true;
                    output.append(c);
                    break;
                case '\\':
                    output.append("\\\\");
                    break;
                case '\n':
                    output.append("\\n");
                    break;
                case '"':
                    output.append(c).append(c);
                    break;
                default:
                    output.append(c);
                    break;
            }
        }
        if (wrapInQuotes){
            return "\"" + output.toString() + "\"";
        }
        return output.toString();
    }

    public static String encodeLine(String ... input)
    {
        StringBuilder output = new StringBuilder();
        if (input.length > 0){
            output.append(encodeString(input[0]));
        }
        for (int i=1; i<input.length; i++){
            output.append(' ');
            output.append(encodeString(input[i]));
        }
        return output.toString();
    }

    public static String[] decodeLine(String input){
        ArrayList<String> output = new ArrayList<String>();
        StringBuilder argument = new StringBuilder();
        char[] inputChars = input.toCharArray();
        boolean insideQuotes = false;

        for (int i=0; i<inputChars.length; i++){
            char c = inputChars[i];
            switch (c){
                case '\\':
                    if (i+1 < inputChars.length){
                        char lookahead = inputChars[i+1];
                        switch (lookahead){
                            case 'n':
                                argument.append('\n');
                                break;
                            default:
                            case '\\':
                                argument.append(lookahead);
                                break;
                        }
                        i++; // consume lookahead
                    }
                    break;
                case '"':
                    // lookahead
                    if (i+1 < inputChars.length && inputChars[i+1] == '\"'){
                        // this is ""
                        argument.append(c);
                        i++;
                    }
                    else {
                        insideQuotes = !insideQuotes;
                    }
                    break;
                case ' ':
                    if (insideQuotes){
                        argument.append(c);
                    } else if (argument.length() > 0){
                        output.add(argument.toString());
                        argument.setLength(0);
                    }
                    break;
                default:
                    argument.append(c);
                    break;
            }
        }
        if (argument.length() > 0){
            output.add(argument.toString());
        }
        return output.toArray(new String[output.size()]);
    }

}
