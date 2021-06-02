import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class Task_manager {
    public static void main(String[] args) {
        String input = "";
        Scanner scan = new Scanner(System.in);
        String[][]taskListArr = loadFile("tasks.csv");
        for (;;) {
            displayMenu();
            input = scan.nextLine();
            switch (input) {
                case "add":
                    taskListArr = addTask(taskListArr);
                    break;
                case "remove":
                    taskListArr = removeTask(taskListArr);
                    break;
                case "list":
                    list(taskListArr);
                    break;
                case "exit":
                    exit(taskListArr, "tasks.csv");
                    break;
                default:
                    System.out.println("Please select a correct option.");
            }
        }
    }

    public static void displayMenu(){
        System.out.println("Please select an option:");
        System.out.println("add");
        System.out.println("remove");
        System.out.println("list");
        System.out.println("exit");
    }

    public static String[][] loadFile(String filename){
        File file = new File(filename);
        String[][] taskListArr = new String[0][3];
        int i = 0;
        try(Scanner scan = new Scanner(file)) {
            while (scan.hasNextLine()) {
                taskListArr = Arrays.copyOf(taskListArr, taskListArr.length + 1);
                String[] parts = scan.nextLine().split(", ");
                taskListArr[taskListArr.length-1] = new String[parts.length];
                for (int j=0; j<parts.length; j++){
                    taskListArr[i][j] = parts[j];
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("file error");
        }
        return taskListArr;
    }

    public static void list(String[][] taskListArr){
        for (int i=0; i<taskListArr.length; i++){
            System.out.print(i+": ");
            for (int k=0; k<3; k++){
                System.out.print(taskListArr[i][k]+" ");
            }
            System.out.println();
        }
    }

    public static String[][] removeTask(String[][]taskListArr) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please select the task to remove");
        int elementToRemove = scan.nextInt();
        while (elementToRemove < 0 || elementToRemove > taskListArr.length - 1) {
            System.out.println("There is no such task");
            System.out.println();
            System.out.println("Please select the task to remove");
            elementToRemove = scan.nextInt();
        }
        for (int i = elementToRemove; i < taskListArr.length - 1; i++) {
            for (int j = 0; j < 3; j++) {
                taskListArr[i][j] = taskListArr[i + 1][j];
            }
        }
        taskListArr = Arrays.copyOf(taskListArr, taskListArr.length - 1);
        return taskListArr;
    }

    public static String[][] addTask(String[][]taskListArr){
        Scanner scan = new Scanner(System.in);
        System.out.println("Please add task");
        System.out.println("In the format 'task, date(year-mm-day), is important(true or false)'");
        String[] parts = scan.nextLine().split(", ");
        taskListArr = Arrays.copyOf(taskListArr, taskListArr.length + 1);

        taskListArr[taskListArr.length-1] = new String[parts.length];

        for (int i=0; i<parts.length; i++){
            taskListArr[taskListArr.length-1][i] = parts[i];
        }
        return taskListArr;
    }
    public static void exit(String[][]taskListArr, String filename){
        String input = new String();
        try(PrintWriter PrintWriter = new PrintWriter(filename)) {
            for (int i=0; i<taskListArr.length; i++){
                input = String.join(", ", taskListArr[i][0], taskListArr[i][1], taskListArr[i][2]);
                PrintWriter.println(input);
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        System.out.println("bye bye");
        System.exit(0);
    }
}
