package peter.azzie;

import android.util.Log;

public class AzzieLog {

    private static String makeString(Object field){
        if (field == null) return "null";
        return field.toString();
    }

    private static String toLogString(Object ... fields){
        String message = null;
        if (fields.length == 1){
            message = makeString(fields[0]);
        }
        else {
            StringBuilder builder = new StringBuilder(makeString(fields[0]));
            for (int i=1; i<fields.length; i++){
                builder.append(" ");
                builder.append(makeString(fields[i]));
            }
            message = builder.toString();
        }
        return message;
    }

    public static void log(Object... fields) {
        android.util.Log.i("AzzieLog", toLogString(fields));
    }

    public static void fail(Object... fields) {
        throw new RuntimeException (toLogString(fields));
    }

}
