package pkg20190771_20190047_20190213;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

/**
 * @author Reem Ehab ,Ahmed Abdelnasser ,Omar Abdallah 
 */
class Parser {

    String commandName;
    String[] args;
    int countSpaces = 0; // a variable to count in it the spaces in a string  
    String[] allComandNames = {"echo", "pwd", "cd", "ls", "ls -r", "mkdir", "rmdir", "touch", "cp", "cp -r", "rm", "cat"};
    boolean flag = true;
//This method will divide the input into commandName and args
//where "input" is the string command entered by the user 

    public boolean parse(String input) {
        int index = 0;
        for (int i = 0; i < input.length(); i++) // for loop to count the number of spaces in the input String
        {
            if (input.charAt(i) == ' ') {
                countSpaces++;
            }
        }
        String[] splittingarray = new String[countSpaces + 1];
        if (countSpaces == 0) // in case the input contains no spaces
        {
            commandName = input;
            checkCommandName(commandName);
        } else {
            splittingarray = input.split(" ");  // spliting the input string into words depending on spaces
            // between the input
            commandName = splittingarray[0];
            if (splittingarray[1].charAt(0) == '-') // checking if the word in index no.1 in the splittingarray contains
            {
                commandName = commandName + ' ';    // " - " for commands like "cp -r" that contains a space.
                commandName = commandName + splittingarray[1];
                index = 1;
                countSpaces--;
            }
            // end of setting the command name 
            if (!this.checkCommandName(commandName)) // checking if the commandName is not in the allComandarray
            {
                flag = false;                                       // " wrong command name "
                return false;   //means that the command name  is wrong 
            } else // if the commandname is in allComandArray "right command Name"
            // so we are setting the arguments int the Arg string 
            {
                args = new String[countSpaces];
                for (int i = 0; i < countSpaces; i++, index++) // foor loop to set the arguments
                {
                    args[i] = splittingarray[index + 1];
                }

            }
        }
        return true;
    }

    public String getCommandName() {
        return commandName;
    }

    public boolean checkCommandName(String input) // function to check if commandName given by the user 
    {                                         //is in array all CommandNames 
        for (int i = 0; i < 12; i++) {
            if (input.equals(allComandNames[i])) {
                return true;
            }

        }
        System.out.println("please type a commandName from below");
        for (int x = 0; x < 12; x++) {
            System.out.print((x + 1) + "-" + allComandNames[x] + " ");
        }
        System.out.println();
        commandName = " there is no commandName";
        return false;
    }

    public String[] getArgs() {
        return args;
    }

    public String returnArgument(int i) {
        return args[i];

    }

    public int getArgumentSize() {
        return countSpaces;
    }

    public void printArguments() {
        if (flag) {
            for (int i = 0; i < countSpaces; i++) {
                System.out.println("argument " + (i + 1) + ' ' + args[i]);
            }

        }
    }
}

public class Terminal {

    static Parser parser;
    static String currentPath = System.getProperty("user.dir");
    static boolean checkdir = false;

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public void echo(String input) {
        System.out.print(input + " ");
    }

    public void pwd() {
        System.out.println("Current Directory = " + currentPath);
    }

    public boolean checkValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
        return true;
    }

    public void mkdir(String input) {
        if (input.contains("\\")) {
            new File(input).mkdirs();
            checkdir = true;

        } else {
            new File(currentPath + "\\" + input).mkdirs();
            checkdir = true;

        }
        if (!checkdir) {
            System.out.println("invalidPath");
            checkdir = false;
        }

    }

    public void ls() {
        File[] files = new File(currentPath).listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isHidden()) {
                continue;
            }
            System.out.print(files[i].getName());
            if (files[i].isDirectory()) {
                System.out.print("\\");
            }
            System.out.println("");
        }
    }

    public void lsr() {
        File[] files = new File(currentPath).listFiles();
        for (int i = files.length - 1; i > 0; i--) {
            if (files[i].isHidden()) {
                continue;
            }
            System.out.print(files[i].getName());
            if (files[i].isDirectory()) {
                System.out.print("\\");//if it is a file 
            }
            System.out.println("");
        }
    }

    public static void cp(String sourcePath, String destinationPath) throws IOException {

        File f1 = new File(sourcePath);
        String absolute1 = f1.getAbsolutePath();

        File f2 = new File(destinationPath);
        String absolute2 = f2.getAbsolutePath();

        try {
            FileReader reader = new FileReader(absolute1);

            try (BufferedReader bufferedReader = new BufferedReader(reader)) {
                FileWriter bufferedWriter = new FileWriter(absolute2, false);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    bufferedWriter.write(line);
                    bufferedWriter.write("\n");
                }
                bufferedWriter.close();
            }
            //  Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied");
        } catch (FileNotFoundException ex) {
            System.out.println("File not Found");
        } catch (IOException ex) {
            System.out.println("cp takes two parameters \n 1: File location \t  2: Destination path ");
        }

    }

    public static void cp_r(String sourcePath, String destinationPath) throws IOException {

        try {
            Files.walk(Paths.get(sourcePath)).forEach(a -> {
                Path b = Paths.get(destinationPath, a.toString().substring(sourcePath.length()));
                try {
                    if (!a.toString().equals(sourcePath)) {
                        Files.copy(a, b, true ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{});
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("Your Directory has been copied!");
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public void Touch(String input) throws IOException {
        if (input.contains("\\")) {

            try {
                File file = new File(input );
                file.createNewFile();

            } catch (IOException e) {
                System.out.println("Invalid path or something went wrong");
            }
        } else {
            File file = new File(currentPath + "\\" + input );
            file.createNewFile();
        }
    }

    public void rm(String[] paths) {

        for (String path : paths) {
            File file = new File(path);
            if (!(file.isAbsolute())) {
                file = new File(currentPath + "\\" + path);
            }
            if (file.exists()) {

                if (file.isFile()) {
                    file.delete();

                } else {
                    System.out.println("ERROR: only files can be deleted!");
                }

            } else {
                System.out.println("ERROR: no such file or directory!");
            }
        }
    }
public void rmdir(String[] input) throws IOException{
  try {
            if (input[0].equals("*")) {
                File currentDirectory = new File(currentPath);
                FileFilter fileFiltering = new FileFilter() //to take only files from the directory
                {
                    public boolean accept(File f) {
                        return f.isDirectory();//if it's a directory
                    }
                };
                File[] arrOfFiles = currentDirectory.listFiles(fileFiltering);//array of files have directories in the current path
                for (File currentFile : arrOfFiles) {
                    String filePath = currentFile.getPath();
                    if (!Files.list(Paths.get(currentFile.getPath())).findAny().isPresent())//to check if the dir is empty or not
                    {
                        Files.delete(Paths.get(filePath));
                    }
                }
            } else {
                Files.delete(Paths.get(input[0]));
            }

        } catch (IOException ex) {
            System.out.println("Directory doesn't exist or it is not empty!");
        }
}

    public static void cat(String path) throws FileNotFoundException {

        try {
            try (FileReader fileReader = new FileReader(path)) {
                BufferedReader in = new BufferedReader(fileReader);
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(path + ", Your File is Not Found");
        } catch (IOException e) {
            System.out.println(path + ", I/O Error.");
        }

    }

    public static void cat(String path1, String path2) throws FileNotFoundException {

        try {
            try (FileReader fileReader = new FileReader(path1)) {
                BufferedReader in = new BufferedReader(fileReader);
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(path1 + ", Your File is Not Found");
        } catch (IOException e) {
            System.out.println(path1 + ", I/O Error.");
        }

        /**
         * *File 1*
         */
        try {
            try (FileReader fileReader = new FileReader(path2)) {
                BufferedReader in = new BufferedReader(fileReader);
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(path2 + ", Your File is Not Found");
        } catch (IOException e) {
            System.out.println(path2 + ", I/O Error.");
        }

    }

    public void cd(String input) throws IOException {

        if (input.equals("..")) {
            Path temp = Paths.get(currentPath);
            Path parentPath = temp.getParent();
            currentPath = parentPath.toString();
        } else if (input.contains("\\")) {

            File file = new File(input);
            if (file.exists()) {
                currentPath = input;

            } else {
                System.out.println("there is no such a path in your PC");
            }
        } else {
            File file = new File(currentPath + "\\" + input);

            if (file.exists()) {
                currentPath = file.getCanonicalPath();
            } else {
                System.out.println("no file or directory!");
            }
        }
    }

    public void cd() throws IOException {

        currentPath = System.getProperty("user.home");
    }

//This method will choose the suitable command method to be called
    public void chooseCommandAction() throws IOException {
        if (parser.getCommandName().equals("echo")) {
            if (parser.getArgumentSize() != 0) {
                for (int i = 0; i < parser.args.length; i++) {
                    this.echo(parser.returnArgument(i));
                }
            } else {
                System.out.println("you must add A string with echo ");
            }

        } else if (parser.getCommandName().equals("pwd")) {

            if (parser.getArgumentSize() == 0) {
                this.pwd();
            } else {
                System.out.println("pwd command don't take arguments ");
            }
        } else if (parser.getCommandName().equals("mkdir")) {
            if (parser.getArgumentSize() == 0) {
                System.out.println(" you must add one or more argument with mkdir command ");
            } else {
                for (int i = 0; i < parser.getArgumentSize(); i++) {
                    this.mkdir(parser.returnArgument(i));
                }
            }
        } else if (parser.getCommandName().equals("ls")) {
            if (parser.getArgumentSize() != 0) {
                System.out.println(" you mustn't add arguments with ls command ");
            } else {
                this.ls();
            }
        } else if (parser.getCommandName().equals("ls -r")) {
            if (parser.getArgumentSize() != 0) {
                System.out.println(" you mustn't add arguments with lsr command ");
            } else {
                this.lsr();
            }
        } else if (parser.getCommandName().equals("cp")) {
            if (parser.getArgumentSize() != 2) {
                System.out.println(" you must add 2 argument with cp command ");
            } else {
                this.cp(parser.returnArgument(0), parser.returnArgument(1));
            }
        } else if (parser.getCommandName().equals("touch")) {
            if (parser.getArgumentSize() != 1) {
                System.out.println(" touch command takes only one argument");
            } else {
                this.Touch(parser.returnArgument(0));
            }
        } else if (parser.getCommandName().equals("rm")) {
            if (parser.getArgumentSize() != 1) {
                System.out.println(" you must add only 1 argument with rm command ");
            } else {
                this.rm(parser.getArgs());
            }
        } else if (parser.getCommandName().equals("rmdir")) {
            if (parser.getArgumentSize() != 1) {
                System.out.println(" you must add only 1 argument with rmdir command ");
            } else {
                this.rmdir(parser.getArgs());
            }
        } else if (parser.getCommandName().equals("cat")) {

            if (parser.getArgumentSize() == 1) {
                for (int i = 0; i < parser.getArgumentSize(); i++) {
                    this.cat(parser.returnArgument(i));
                }
            } else if (parser.getArgumentSize() == 2) {
                this.cat(parser.returnArgument(0), parser.returnArgument(1));
            } else {
                System.out.println(" you must add one or more argument with cat command ");
            }
        } else if (parser.getCommandName().equals("cd")) {
            if (parser.getArgumentSize() == 0) {
                this.cd();
            } else if (parser.getArgumentSize() == 1) {
                this.cd(parser.returnArgument(0));

            } else {
                System.out.println(" bad parameters");
            }

        } else if (parser.getCommandName().equals("cp -r")) {
            if (parser.getArgumentSize() != 2) {
                System.out.println(" you must add 2 argument with cp -r command ");
            } else {
                this.cp_r(parser.returnArgument(0), parser.returnArgument(1));
            }
        } else {
            System.out.println("this command isn't implemented yet ");
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("type an input");
            String input = scan.nextLine();
            if (input.equals("exit")) {
                break;
            }
            Parser p = new Parser();
            p.parse(input);
            System.out.println("command name is " + p.getCommandName());
            p.printArguments();
            Terminal T = new Terminal();
            T.setParser(p);
            T.chooseCommandAction();
            System.out.println("_________");

        }
    }
}
