import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try (FileWriter writer = new FileWriter("result.txt", false)) {
            BufferedReader reader = new BufferedReader(new FileReader("algorithm.txt"));
            ArrayList<String> rules = new ArrayList<>();
            String line = "";
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                line = nextLine;
                if (line.contains("//")) {
                    line = line.substring(0, line.indexOf("//"));
                }
                if (line.contains("->")) {
                    rules.add((line).trim());
                }
            }
            for (int i = 0; i < rules.size(); i++) {
                String rule = Integer.toString(i + 1).indent(3 - (Integer.toString(i + 1).length())).replace("\n", "") + " : " + rules.get(i) + '\n';
                writer.write(rule);
            }
            writer.append('\n');
            writer.write("--- : " + line);
            writer.write("\n\n");
            String a;
            String b;
            boolean finalRule = false;
            boolean overflow = false;
            boolean replaced;
            while (true) {
                replaced = false;
                for (int i = 0; i < rules.size(); i++) {
                    String[] parts = rules.get(i).split("->");
                    if (parts.length == 1) {
                        parts = new String[] {parts[0], ""};
                    }
                    a = parts[0].trim();
                    b = parts[1].trim();
                    if (line.contains(a)) {
                        replaced = true;

                        if (b.length() != 0 && b.charAt(b.length() - 1) == 'x') {
                            finalRule = true;
                            b = b.substring(0, b.length() - 1);
                        }
                        writer.write(line);
                        writer.append('\n');
                        writer.write(a.indent(line.indexOf(a)).replace("\n", "") + " -" + (i + 1) + "-> " + b + '\n');
                        if (a.equals("")) {
                            line = b + line;
                        } else {
                            line = (line.substring(0, line.indexOf(a) + a.length())).replace(a, b) + line.substring(line.indexOf(a) + a.length());
                        }
                        if (line.length() > 255) {
                            overflow = true;
                        }
                        break;
                    }
                }
                if (finalRule) {
                    writer.write("***__FINAL_RULE__***\n\n");
                    break;
                }
                if (overflow) {
                    writer.write("***__OVERFLOW__***\n\n");
                    break;
                }
                if (!replaced) {
                    writer.write("***__NO_MORE_RULES__***\n\n");
                    break;
                }
            }
            writer.write(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}