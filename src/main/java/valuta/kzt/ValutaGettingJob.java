package valuta.kzt;

import org.json.JSONObject;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by BahaWood on 1/27/19.
 */

@Component
public class ValutaGettingJob extends QuartzJobBean{
    @Autowired
    private ValutaRepository valutaRepository;

    private String url = "http://free.currencyconverterapi.com/api/v5/convert?q=%s&compact=y";

    private String EUR_KZT = "EUR_KZT";
    private String USD_KZT = "USD_KZT";
    private String RUB_KZT = "RUB_KZT";

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            convertCurrency(EUR_KZT);
            convertCurrency(USD_KZT);
            convertCurrency(RUB_KZT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convertCurrency(String fromTo) throws IOException {

        URL obj = new URL(String.format(url, fromTo));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject myResponse = new JSONObject(response.toString());
        String usd = myResponse.getJSONObject(fromTo).get("val").toString();

        ValutaModel model = new ValutaModel();
        model.setDate(new Date());
        model.setFromTo(fromTo);
        model.setRate(Float.parseFloat(usd));

        valutaRepository.save(model);

    }
}
