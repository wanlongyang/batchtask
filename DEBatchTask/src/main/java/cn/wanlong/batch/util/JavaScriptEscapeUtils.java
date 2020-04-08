package cn.wanlong.batch.util;

public class JavaScriptEscapeUtils
{
  public static String javaScriptEscape(String input)
  {
    if (input == null) {
      return input;
    }

    StringBuilder filtered = new StringBuilder(input.length());

    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      switch (c) {
      case '<':
        filtered.append("&lt;");
        break;
      case '>':
        filtered.append("&gt;");
        break;
      case '&':
        filtered.append("&amp;");
        break;
      case '"':
        filtered.append("&quot;");
        break;
      case '\n':
      case '\r':
        break;
      default:
        filtered.append(c);
      }
    }

    return filtered.toString();
  }
}