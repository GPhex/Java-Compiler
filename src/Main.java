import ast.ASTNode;
import codeGeneration.IntermediateCodeGenerator;
import codeGeneration.MainCodeGenerator;
import codeGeneration.environment.NullEnvironment;

import java.io.*;
import java.util.Arrays;

public class Main {

    private static final String COMPILER_NAME = "BetterJava";
    private static final String FILE_TAG = "-f";
    private static final String INTERPRET_TAG = "-i";
    private static final String DEBUG_TAG = "-d";

    public static void main(String[] args) {
        final CompileDetails mode = interpretArguments(args);
        if(mode == null) {
            printHelp();
            System.exit(-1);
        }
        final String outputDirectory = ".";
        final int cleanedFiles = cleanDirectory(outputDirectory, new String[] {".j", ".class"});
        if(cleanedFiles > 0)
            System.out.printf("Cleaned output directory: %d files removed\n", cleanedFiles);
        Parser parser = new Parser(mode.file);
        if(mode.interpret) {
            if(mode.fileName == null) {
                while(true) {
                    System.out.println("In expression:");
                    interpret(parser);
                    parser.ReInit(mode.file);
                }
            }
            else {
                System.out.printf("Interpreting file %s\n", mode.fileName);
                interpret(parser);
            }
        }
        else {
            System.out.printf("Compiling file %s\n", mode.fileName);
            final String outputFileName = getOutputFileName(mode.fileName);
            ASTNode tree = parseOnce(parser);
            if(tree != null) {
                try {
                    IntermediateCodeGenerator generator = new MainCodeGenerator(outputFileName);
                    tree.compile(new NullEnvironment<>(), generator);
                    generator.printTopStack();
                    final String[] fileNames = generator.dumpClass();
                    for(String file : fileNames)
                        runJasmin(file);
                    if(!mode.debug)
                        cleanDirectory(outputDirectory, new String[] {".j"});
                    System.out.println("Compilation successful!");
                }
                catch (Exception e) {
                    System.out.println("Problem encountered during compilation:");
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    private static void printHelp() {
        System.out.printf("Usage: java -jar %s.jar [OPTIONS]\n", COMPILER_NAME);
        System.out.println("[OPTIONS]: ");
        System.out.println("-f file: compiles the provided file");
        System.out.println("-i: force interpreter mode. If a valid -f option is present, interprets given file and finishes");
        System.out.println("-d: keep all .j files");
    }

    private static CompileDetails interpretArguments(final String[] args) {
        if(args.length > 3)
            return null;
        boolean debugTag = false;
        boolean fileTag = false;
        boolean interpret = true;
        String filePath = null;
        for (String arg : args) {
            switch (arg) {
                case FILE_TAG:
                    fileTag = true;
                    interpret = false;
                    break;
                case INTERPRET_TAG:
                    interpret = true;
                    break;
                case DEBUG_TAG:
                    debugTag = true;
                    break;
                default:
                    if (fileTag) {
                        filePath = arg;
                        fileTag = false;
                    }
                    break;
            }
        }
        if(fileTag)
            return null;
        if(filePath != null) {
            try {
                InputStream fileStream = new FileInputStream(filePath);
                if(!(interpret && debugTag))
                    return new CompileDetails(filePath, fileStream, interpret, debugTag);
            }
            catch (FileNotFoundException e) {
                System.err.printf("File %s does not exist\n", filePath);
            }
        }
        else if(!debugTag)
            return new CompileDetails(null, System.in, true, false);
        return null;
    }

    private static int cleanDirectory(final String relativeDirectory, final String[] removeFilesExtensions) {
        final File curDirectory = new File(relativeDirectory);
        final File[] files = curDirectory.listFiles();
        int cleanedFiles = 0;
        if(files != null) {
            for(File file : files) {
                if(Arrays.stream(removeFilesExtensions).anyMatch(s -> file.getName().endsWith(s)))
                    if(file.delete())
                        cleanedFiles++;
            }
        }
        return cleanedFiles;
    }

    private static String getOutputFileName(final String fileName) {
        int extensionIndex = fileName.lastIndexOf(".");
        if(extensionIndex == -1)
            extensionIndex = fileName.length();
        return fileName.substring(fileName.lastIndexOf("/") + 1, extensionIndex);
    }

    private static ASTNode parseOnce(final Parser parser) {
        try {
            return parser.Start();
        }
        catch (ParseException e) {
            System.out.println("Syntax error!");
            e.printStackTrace(System.err);
            return null;
        }
    }

    private static void interpret(Parser parser) {
        ASTNode tree = parseOnce(parser);
        try {
            if(tree != null) System.out.printf("Out value: %d\n", tree.evaluate(new NullEnvironment<>()));
        }
        catch (Exception e) {
            System.out.println("Interpretation problem:");
            e.printStackTrace(System.err);
        }
    }

    private static void runJasmin(final String fileName) throws IOException, InterruptedException {
        Process jasmin = Runtime.getRuntime().exec(new String[] {"java", "-jar", "jasmin.jar", fileName});
        jasmin.waitFor();
        if(jasmin.exitValue() != 0)
            throw new RuntimeException(String.format("Could not compile file %s with jasmin: Jasmin must be in the same directory as this jar!", fileName));
    }

    static class CompileDetails {

        private final String fileName;
        private final InputStream file;
        private final boolean interpret;
        private final boolean debug;

        CompileDetails(final String fileName, final InputStream file, final boolean interpret, final boolean debug) {
            this.fileName = fileName;
            this.file = file;
            this.interpret = interpret;
            this.debug = debug;
        }
    }
}
