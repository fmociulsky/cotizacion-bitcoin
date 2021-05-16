package examen.wenance.bitcoincotizacionapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import examen.wenance.bitcoincotizacionapi.model.PairPrice;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CotizacionRestBuilder {

    public static Double getCotizacionFromUrl(String _url) {
        Double value = 0d;
        final HttpClient client = new DefaultHttpClient();
        final HttpGet request = new HttpGet(_url);
        try {
            final HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != HttpStatus.OK.value()) return value;
            final HttpEntity entity = response.getEntity();

            if (entity != null) {
                final InputStream instream = entity.getContent();
                final String result = convertStreamToString(instream);
                final ObjectMapper objectMapper = new ObjectMapper();
                final PairPrice pairPrice = objectMapper.readValue(result, PairPrice.class);

                instream.close();
                value = Double.valueOf(pairPrice.getLprice());

            }
        } catch (final IOException e1) {
            System.out.println(e1.getMessage());
        }
        return value;
    }

    private static String convertStreamToString(InputStream is) {

        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        final StringBuilder sb = new StringBuilder();

        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
