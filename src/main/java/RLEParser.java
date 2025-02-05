import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;


public class RLEParser {
    @Option(name = "-z", metaVar = "Packager", forbids = {"-u"})
    private boolean toPackager;

    @Option(name = "-u", metaVar = "Unpackager", forbids = {"-z"})
    private boolean toUnpackager;

    @Option(name = "-out", metaVar = "PackagerOutput")
    private String outputName;

    @Argument(required = true, metaVar = "PackagerInput")
    private String inputName;



    public static void main(String[] args) {
        try {
            new RLEParser().launch(args);
        } catch (IllegalArgumentException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void launch(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("Example: pack-rle [-z|-u] [-out outputname.txt] inputname.txt");
            parser.printUsage(System.err);
            return;
        }

        RLE RLE = new RLE();
        String line;
        try (BufferedReader in = new BufferedReader(new FileReader(inputName))) {
            try (BufferedWriter out = new BufferedWriter(new FileWriter(outputName))) {
                while ((line = in.readLine()) != null) {
                    if (toPackager) out.write(RLE.packager(line));
                    else out.write(RLE.unpackager(line));
                    out.newLine();
                }
            }
        }
    }
}