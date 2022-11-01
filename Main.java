import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Scanner;

/***
 * This is the Main class that calls other class methods.
 *
 * @author khanhle
 *
 */
public class Main {
    /***
     * The main function is used to prompt the user and get the choice of input to
     * perform either look for next holiday or a list of holidays.
     *
     * @param args main
     */
    public static void main(String[] args) {
        try (Scanner keyboard = new Scanner(System.in)) {
            final int firstChoice = 1; // Option 1 - tell the program to retrieve the next holiday
            final int secondChoice = 2; /**
                                         * Option 2 - tell the program to retrieve the list of holidays in the given
                                         * year
                                         **/
            MyHolidayLookUp lookUp = new MyHolidayLookUp(); /**
                                                             * lookUp is an object of type MyHolidayLookUp used to
                                                             * access method in the class
                                                             **/
            Province province; // an object of type Province used to store postal code and name of a province
            Holiday holiday = null; // an object of type Holiday used to store holiday name and date of holiday

            System.out.println("Welcome to the holiday checker!");
            System.out.print("Do you want (1) the next holiday or (2) a list of holidays for a year? ");
            int answer = keyboard.nextInt();
            String provinceName; // an object of type String to store the name of province

            if (answer == firstChoice) {
                System.out.print("Enter province (by postal abbreviation, e.g. AB): ");
                provinceName = keyboard.next();
                LocalDate currentDate = LocalDate.now(); // an object of type LocalDate used to store the current date

                province = Province.getProvinceFromPostal(provinceName);

                if (province.toString() == "Invalid") {
                    System.out.println("Opps - You enter non-existing province !!");
                } else {
                    holiday = lookUp.getNextHoliday(currentDate, province);
                    if (holiday == null) {
                        System.out.println("BAD FOR YOU - NO HOLIDAY RETRIEVED -_-!");
                    } else {
                        String formattedDate = holiday.getDate()
                                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
                        System.out.println("The next holiday in " + province + " is " + holiday.getName() + " on "
                                + formattedDate);
                    }
                }
            } else if (answer == secondChoice) {
                System.out.print("Enter year to retrieve: ");
                int year = keyboard.nextInt();
                System.out.print("Enter province (by postal abbreviation, e.g. AB): ");
                provinceName = keyboard.next();

                province = Province.getProvinceFromPostal(provinceName);

                if (province.toString() == "Invalid") {
                    System.out.println("Opps - You enter non-existing province !!");
                } else {
                    List<Holiday> holidayList = lookUp.getHolidays(year, province);
                    if (holidayList.isEmpty()) {
                        System.out.println("NO LIST OF HOLIDAYS RETRIEVED -_-");
                    } else {
                        System.out.println("The " + year + " holidays in " + province.toString() + " are: ");
                        for (int i = 0; i < holidayList.size(); i++) {
                            String formattedDate = holidayList.get(i).getDate()
                                    .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
                            System.out.print("  - " + formattedDate + " : ");
                            System.out.println(holidayList.get(i).getName());
                        }
                    }
                }
            } else {
                System.out.println("Opps - You enter invalid option !!!!");
            }
        }
        System.out.println("BYE BYE - HAVE A GOOD DAY ^-^ !!");
    }
}
