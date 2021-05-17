package examen.wenance.bitcoincotizacionapi.dao;


import examen.wenance.bitcoincotizacionapi.model.BitcoinValue;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BitcoinValueDao extends JpaRepository<BitcoinValue, Date> {

    BitcoinValue findByFechaCotizacion(LocalDateTime fechaCotizacion);

    @Query(value = "SELECT * FROM BITCOIN_VALUE WHERE FECHA_COTIZACION >= :startDate AND FECHA_COTIZACION <= :endDate", nativeQuery = true)
    List<BitcoinValue> getCotizacionesEntreFechas(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);
}
