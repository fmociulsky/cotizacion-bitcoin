package examen.wenance.bitcoincotizacionapi.service;

import examen.wenance.bitcoincotizacionapi.dao.BitcoinValueDao;
import examen.wenance.bitcoincotizacionapi.model.BitcoinValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BitcoinValueService {

    @Autowired
    BitcoinValueDao bitcoinValueDao;

    public BitcoinValueService() {
    }

    public List<String> getAllDates() {
        return bitcoinValueDao.findAll()
                .stream()
                .map(bitcoinValue ->
                        Timestamp.valueOf(bitcoinValue.getFechaCotizacion())
                                .toString().replace("T", " "))
                .collect(Collectors.toList());
    }

    public Double getCotizacionPorFecha(LocalDateTime localDateTime) {
        final BitcoinValue byFechaCotizacion = bitcoinValueDao.findByFechaCotizacion(localDateTime);
        if(byFechaCotizacion != null) return byFechaCotizacion.getCotizacion();
        return null;
    }

    public Double getCotizacionPorFechaStream(LocalDateTime localDateTime) {
        return bitcoinValueDao.findAll()
                .stream()
                .filter(bitcoinValue -> bitcoinValue.getFechaCotizacion().equals(localDateTime))
                .mapToDouble(BitcoinValue::getCotizacion)
                .sum();
    }

    public Double getPromedioCotizacionEntreFechasStream(LocalDateTime localDateTimeDesde, LocalDateTime localDateTimeHasta) {
        return bitcoinValueDao.findAll()
                .stream()
                .filter(bv->
                        bv.getFechaCotizacion().equals(localDateTimeDesde) ||
                        (bv.getFechaCotizacion().isAfter(localDateTimeDesde)
                                && bv.getFechaCotizacion().isBefore(localDateTimeHasta)) ||
                        bv.getFechaCotizacion().equals(localDateTimeHasta)

                )
                .mapToDouble(BitcoinValue::getCotizacion)
                .average()
                .orElse(0d);
    }
}
