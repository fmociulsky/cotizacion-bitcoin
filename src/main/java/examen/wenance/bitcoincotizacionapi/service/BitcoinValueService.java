package examen.wenance.bitcoincotizacionapi.service;

import examen.wenance.bitcoincotizacionapi.dao.BitcoinValueDao;
import examen.wenance.bitcoincotizacionapi.model.BitcoinAverageInfo;
import examen.wenance.bitcoincotizacionapi.model.BitcoinValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

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

    public BitcoinAverageInfo getPromedioCotizacionEntreFechasStream(LocalDateTime localDateTimeDesde, LocalDateTime localDateTimeHasta) {

        final Supplier<Stream<BitcoinValue>> streamSupplier = () -> bitcoinValueDao.findAll().stream().filter(bv ->
                bv.getFechaCotizacion().equals(localDateTimeDesde) ||
                        (bv.getFechaCotizacion().isAfter(localDateTimeDesde)
                                && bv.getFechaCotizacion().isBefore(localDateTimeHasta)) ||
                        bv.getFechaCotizacion().equals(localDateTimeHasta));

        final double max = streamSupplier.get().mapToDouble(BitcoinValue::getCotizacion).max().orElse(0d);
        final double average = streamSupplier.get().mapToDouble(BitcoinValue::getCotizacion).average().orElse(0d);

        final BitcoinAverageInfo bitcoinAverageInfo = new BitcoinAverageInfo();
        bitcoinAverageInfo.setAverage(average);
        bitcoinAverageInfo.calculatePercent(max);

        return bitcoinAverageInfo;
    }
}
