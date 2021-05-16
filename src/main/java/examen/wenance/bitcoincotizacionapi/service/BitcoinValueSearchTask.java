package examen.wenance.bitcoincotizacionapi.service;

import examen.wenance.bitcoincotizacionapi.dao.BitcoinValueDao;
import examen.wenance.bitcoincotizacionapi.model.BitcoinValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BitcoinValueSearchTask {

    @Autowired
    BitcoinValueDao bitcoinValueDao;

    @Scheduled(fixedRate = 5000)
    public void saveBitcoinValue(){
        final Double cotizacion = CotizacionRestBuilder.getCotizacionFromUrl(URL);
        if(cotizacion != null){
            final BitcoinValue bitcoinValueSave = new BitcoinValue(LocalDateTime.now(), cotizacion);
            bitcoinValueDao.save(bitcoinValueSave);
        }
    }

    private static final String URL ="https://cex.io/api/last_price/BTC/USD";
}
