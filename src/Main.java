import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            File irs_file = new File("IRSdata.dat");
            Scanner scanner = new Scanner(irs_file);
            LinkedList<Employer> empList = new LinkedList<Employer>();

            for (int i = 0; i < 1200; i++) {
                String data = scanner.nextLine();
                //System.out.println(data.substring(2, 18));

                //Adding to a link list
                if (i > 0) {
                    String EINnum = data.substring(2, 11);
                    String tax_pd = data.substring(12, 18);
                    //System.out.println(EINnum + " " + tax_pd);
                    Employer emp = new Employer(EINnum, tax_pd);
                    empList.add(emp);
                }

            }

            System.out.println(empList);

            Scanner usr_scanner = new Scanner(System.in);
            System.out.println("Enter a EIN to search: ");
            String user_input = usr_scanner.nextLine();

            int found_emp = -1;
            for (int i = 0; i < empList.size(); i++) {
                Employer tempE = empList.get(i);
                if (user_input.equals(tempE.getEINnum())) {
                    System.out.println(tempE);
                    found_emp = 0;
                    break;
                }
            }

            if (found_emp == -1) {
                System.out.println("Employer does not exist.");
            }
//            while (scanner.hasNextLine()) {
//                String data = scanner.nextLine();
//                System.out.println(data);
//            }
        }
        catch (FileNotFoundException e) {
            System.out.println("No file with the matching name");
            e.printStackTrace();
        }
    }
}
