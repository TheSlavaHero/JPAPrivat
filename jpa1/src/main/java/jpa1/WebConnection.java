package jpa1;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class WebConnection {
    private static final String URL_PRIVAT = "https://api.privatbank.ua/p24api/exchange_rates?json&date=";

    public static String getJsonfromPB(Date date) throws IOException {
        String json = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateStr = simpleDateFormat.format(date);
        Scanner sc = new Scanner(new URL(URL_PRIVAT + dateStr).openStream());//date in format dd.MM.yyyy from year 2014.
        if (sc.hasNextLine()) {
            json = json + sc.nextLine();
        }
        return json;
    }
}
