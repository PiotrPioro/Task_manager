import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
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
        System.out.println(ConsoleColors.BLUE + "Please select an option:");
        System.out.print(ConsoleColors.RESET);
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
        taskListArr = ArrayUtils.remove(taskListArr, taskListArr.length-1);
        //taskListArr = Arrays.copyOf(taskListArr, taskListArr.length - 1); //metoda alternatywna do poprzedniej linii
        return taskListArr;
    }

    public static String[][] addTask(String[][]taskListArr){
        String task = "";
        String date = "";
        String important = "";
        Scanner scan = new Scanner(System.in);

        System.out.println("Please add task");
        task = scan.nextLine();

        date = dateCheck();

        System.out.println("Is the task important?(true/false)");
        important = scan.nextLine();
        while (!(important.equals("true") || important.equals("false"))){
            System.out.println("Wrong value!");
            System.out.println();
            System.out.println("Is the task important?(true/false)");
            important = scan.nextLine();
        }
        taskListArr = Arrays.copyOf(taskListArr, taskListArr.length + 1);
        taskListArr[taskListArr.length-1] = new String[3];
        taskListArr[taskListArr.length-1][0] = task;        //to mogę dodać za pomocą pętli, ale w tym przypadku kod byłby dłuższy
        taskListArr[taskListArr.length-1][1] = date;
        taskListArr[taskListArr.length-1][2] = important;

        return taskListArr;
    }

    public static String dateCheck (){          //Tą część próbowałem zrobić za pomocą metody LocalData.isSuported(), ale nie wyszło
        Scanner scan = new Scanner(System.in);
        LocalDate localDate = LocalDate.now();
        String date = "";
        int year = 1000, month = 1, day = 1;
        LocalDate inputDate = LocalDate.of(year, month, day);

        while (inputDate.isBefore(localDate)){
            System.out.println("Please add year");
            year = Integer.parseInt(scan.nextLine());
            while(year > 999999999 || year < -999999999){ //takie wartości znalazłem w opisie metod "LocalData"
                System.out.println("Invalid year");
                System.out.println("Please add year");
                year = Integer.parseInt(scan.nextLine());
            }

            System.out.println("Please add month");
            month = Integer.parseInt(scan.nextLine());
            while(month > 12 || month < 1){
                System.out.println("Invalid month");
                System.out.println("Please add month");
                month = Integer.parseInt(scan.nextLine());
            }

            boolean goodDay = false;
            while(!goodDay){
                System.out.println("Please add day");
                day = Integer.parseInt(scan.nextLine());
                switch (month){
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        if (day > 0 && day < 32){
                            goodDay = true;
                        }
                        break;
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        if (day > 0 && day < 31){
                            goodDay = true;
                        }
                        break;
                    case 2:
                        if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)){
                            if (day > 0 && day < 30){
                                goodDay = true;
                            }
                        }
                        else if (day > 0 && day < 29){
                                goodDay = true;
                            }
                }
                if (goodDay == false){
                    System.out.println("Invalid day");
                }
            }

            inputDate = LocalDate.of(year, month, day);
            if (inputDate.isBefore(localDate)){
                System.out.println("This date has already been");
            }
        }
        date = String.join("-", year+"", month+"", day+"");
        return date;
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
        System.out.println(ConsoleColors.RED + "bye bye");
        System.out.print(ConsoleColors.RESET);
        System.exit(0);
    }
}
