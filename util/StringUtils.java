package util;

public class StringUtils {

    public static String trimWhitespace(String var0)
    {
        if (var0 == null) {
            return var0;
        } else {
            StringBuffer var1 = new StringBuffer();

            for(int var2 = 0; var2 < var0.length(); ++var2) {
                char var3 = var0.charAt(var2);
                if (var3 != '\n' && var3 != '\f' && var3 != '\r' && var3 != '\t') {
                    var1.append(var3);
                }
            }

            return var1.toString().trim();
        }
    }
}
