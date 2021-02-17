package jpa1;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Main {

    public static void main(String[] args) {
        long currentTime = System.currentTimeMillis();
        long day = 86400000;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAPrivat");
        EntityManager em = emf.createEntityManager();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            for (long i = 0; i < day * 30; i += day) {
                Date date = new Date(currentTime - i);
                String json = WebConnection.getJsonfromPB(date);
                CurrencyData currencyData = CurrencyData.fromJson(json);
                Currency[] curArr = currencyData.getExchangeRate();
                for (Currency currency : curArr) {
                    Date date2 = simpleDateFormat.parse(currencyData.getDate());
                    currency.setDate(date2);
                    currency.setId((int) (i/day));
                    try {
                        if (currency.getCurrency().equals("EUR")) {
                            em.getTransaction().begin();
                            em.persist(currency);
                            em.getTransaction().commit();
                        }
                    } catch (NullPointerException e) {}
                }
                Query query = em.createQuery("SELECT MAX(saleRate)FROM Currency");
                System.out.println(query.getSingleResult());
                query = em.createQuery("SELECT AVG(saleRate)FROM Currency");
                System.out.println(query.getSingleResult());
            }
        } catch (IOException | ParseException e) { e.printStackTrace(); }
    }
}
