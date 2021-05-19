package examen.wenance.bitcoincotizacionapi.controller;

import examen.wenance.bitcoincotizacionapi.service.BitcoinValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

import static examen.wenance.bitcoincotizacionapi.controller.ControllerMessages.DATES_ARE_WRONG;
import static examen.wenance.bitcoincotizacionapi.controller.ControllerMessages.DATES_FORMAT_WRONG;
import static examen.wenance.bitcoincotizacionapi.controller.ControllerMessages.VALUE_NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/cotizaciones")
public class BitcoinCotizacionController {

    @Autowired
    BitcoinValueService bitcoinValueService;

    @GetMapping("")
    public ResponseEntity<?> getCotizacionPorFecha(@RequestParam(value = "fecha_exacta") String fecha_exacta){
        final Timestamp timestamp;
        try{
            timestamp = Timestamp.valueOf(fecha_exacta);
        }catch (final IllegalArgumentException exception){
            return new ResponseEntity<String>(DATES_FORMAT_WRONG.getValue(), BAD_REQUEST);
        }
        final Double value = bitcoinValueService.getCotizacionPorFechaStream(timestamp.toLocalDateTime());
        if(value != 0d) return new ResponseEntity<Double>(value, OK);
        return new ResponseEntity<String>(VALUE_NOT_FOUND.getValue(), NOT_FOUND);
    }

    @GetMapping("/promedio")
    public ResponseEntity<?> getPromedioCotizacionEntreFechas(@RequestParam(value = "fecha_desde") String fecha_desde, @RequestParam(value = "fecha_hasta") String fecha_hasta){
        final Timestamp timestampDesde;
        final Timestamp timestampHasta;
        try{
            timestampDesde = Timestamp.valueOf(fecha_desde);
            timestampHasta = Timestamp.valueOf(fecha_hasta);
        }catch (final IllegalArgumentException exception){
            return new ResponseEntity<String>(DATES_FORMAT_WRONG.getValue(), BAD_REQUEST);
        }

        if(timestampDesde.after(timestampHasta)) return new ResponseEntity<String>(DATES_ARE_WRONG.getValue(), BAD_REQUEST);
        final Double value = bitcoinValueService.getPromedioCotizacionEntreFechasStream(timestampDesde.toLocalDateTime(), timestampHasta.toLocalDateTime());
        if(value != 0d) return new ResponseEntity<Double>(value, OK);
        return new ResponseEntity<String>(VALUE_NOT_FOUND.getValue(), NOT_FOUND);
    }

    @GetMapping("/registro")
    public ResponseEntity<List<String>> getFechas(){
        final List<String> fechas = bitcoinValueService.getAllDates();
        return new ResponseEntity<List<String>>(fechas, OK);
    }

}
