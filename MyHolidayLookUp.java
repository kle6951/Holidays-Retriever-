import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/***
 * This MyHolidayLookUp implements the HolidayLookUp interface.
 *
 * @author khanhle
 *
 */
public class MyHolidayLookUp implements HolidayLookup {
    /***
     * This function is implemented for retrieving the list of data from the
     * provided URL and parse the data to store in List of type Holiday to return a
     * list of holidays in the provided year in the provided province from the users
     * as requested.
     *
     * @author khanhle
     */
    @Override
    public List<Holiday> getHolidays(int year, Province province) {
        List<Holiday> holidayList = new ArrayList<Holiday>();
        /**
         * an array list of type Holiday to store a list of holidays. Each index stores
         * an object of type Holidays
         **/
        final int substringIndex = 3;
        /**
         * the index to retrieve the postal code of the province from the data loaded
         * from the URL
         **/

        /***
         * This part of the code below is responsible for retrieving, parsing and store
         * a list of holidays come from the provided URL. The retrieved data will be
         * store in a List of type NagerHoliday.
         */

        final String url = String.format("https://date.nager.at/api/v2/publicholidays/%d/CA", year);
        Gson gson = new Gson();
        try (Reader jsonRead = new InputStreamReader(new URL(url).openStream())) {
            Type listType = new TypeToken<List<NagerHoliday>>() {
            }.getType();
            List<NagerHoliday> nagerHolidays = gson.fromJson(jsonRead, listType);

            for (int i = 0; i < nagerHolidays.size(); i++) {
                if (nagerHolidays.get(i).global == false) {
                    int countiesLength = nagerHolidays.get(i).counties.length;
                    boolean sameProvince = false;

                    for (int index = 0; index < countiesLength; index++) {
                        String holidayProvince = nagerHolidays.get(i).counties[index].substring(substringIndex);
                        sameProvince = province.getCode().equals(holidayProvince);
                        if (sameProvince) {
                            LocalDate date = LocalDate.parse(nagerHolidays.get(i).date);
                            holidayList.add(new Holiday(nagerHolidays.get(i).name, date));
                        }
                    }

                } else if (nagerHolidays.get(i).global == true) {
                    LocalDate date = LocalDate.parse(nagerHolidays.get(i).date);
                    holidayList.add(new Holiday(nagerHolidays.get(i).name, date));
                }
            }
        } catch (IOException ex) {
            // Catch
        }
        ;
        return holidayList;
    }

    /***
     * This function getNextHoliday is implemented for retrieving the next holiday
     * after the provided date in the provided province in the input parameter. The
     * function calls the getHolidays method first to retrieve a list of holidays in
     * a year first. After that, the function will search for the next holiday after
     * the provided in list and return object of type Holiday that stores the date
     * back to caller.
     *
     * @author khanhle
     */

    @Override
    public Holiday getNextHoliday(LocalDate date, Province province) {

        boolean found = false; // true if the next holiday is found, otherwise false
        Holiday holiday = null;
        /**
         * an object of type Holiday used to store the retrieved holiday null because no
         * holiday has been found
         **/

        final int december = 12; // an integer that stores the last month of the year
        final int firstIndex = 0; // the first index of the list used to retrieve the first holiday of the year
        LocalDate holidayDate; // used to store holiday date

        int year = date.getYear(); // retrieve the year from the provided date

        List<Holiday> listOfHolidays = getHolidays(year, province);
        /**
         * assign the retrieved list of holidays to list named listOfHolidays
         **/

        for (int i = 0; i < listOfHolidays.size(); i++) {
            if (!found) {
                if ((date.getMonthValue() == december)
                        && (date.getDayOfMonth() >= listOfHolidays.get(i).getDate().getDayOfMonth())) {
                    List<Holiday> listOfHolidaysNextYear = getHolidays(year + 1, province);
                    holiday = listOfHolidaysNextYear.get(firstIndex);
                    found = true;
                } else if (date.isEqual(listOfHolidays.get(i).getDate())) {
                    holiday = listOfHolidays.get(i);
                    found = true;
                } else if ((date.getMonthValue() == listOfHolidays.get(i).getDate().getMonthValue())
                        && (date.getDayOfMonth() != listOfHolidays.get(i).getDate().getDayOfMonth())) {
                    holidayDate = listOfHolidays.get(i).getDate();
                    if (holidayDate.isAfter(date)) {
                        holiday = listOfHolidays.get(i);
                        found = true;
                    }
                } else if (date.getMonthValue() != listOfHolidays.get(i).getDate().getMonthValue()) {
                    holidayDate = listOfHolidays.get(i).getDate();
                    if (holidayDate.isAfter(date)) {
                        holiday = listOfHolidays.get(i);
                        found = true;
                    }
                }
            }
        }
        return holiday;
    }

    private static class NagerHoliday {
        String date;
        String name;
        boolean global;
        String[] counties;
    }
}
