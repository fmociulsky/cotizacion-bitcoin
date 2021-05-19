package examen.wenance.bitcoincotizacionapi.controller;

import examen.wenance.bitcoincotizacionapi.BitcoinCotizacionApiApplication;
import examen.wenance.bitcoincotizacionapi.dao.BitcoinValueDao;
import examen.wenance.bitcoincotizacionapi.model.BitcoinValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static examen.wenance.bitcoincotizacionapi.controller.ControllerMessages.DATES_ARE_WRONG;
import static examen.wenance.bitcoincotizacionapi.controller.ControllerMessages.DATES_FORMAT_WRONG;
import static examen.wenance.bitcoincotizacionapi.controller.ControllerMessages.VALUE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(classes = BitcoinCotizacionApiApplication.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class BitcoinCotizacionControllerTest {

    @Autowired
    BitcoinCotizacionController bitcoinCotizacionController;

    @Autowired
    BitcoinValueDao bitcoinValueDao;

    private static LocalDateTime startTime;
    private static boolean runOnlyOnce = true;

    @Before
    public void setUp(){
        if(runOnlyOnce){
            startTime = LocalDateTime.now();
            setUp(startTime);
            runOnlyOnce = false;
        }
    }

    @Test
    public void getCotizacionPorFechaTest(){
        for (int i = 0; i < 10; i++) {
            final ResponseEntity<?> result = bitcoinCotizacionController.getCotizacionPorFecha(buildStringTimestamp(startTime.plusSeconds(i * 5)));
            assertThat(result.getStatusCode()).isEqualTo(OK);
            assertThat(result.getBody()).isEqualTo((double) i+1);
        }
    }

    @Test
    public void getCotizacionPorFechaWrongFormatTest(){
        final ResponseEntity<?> result = bitcoinCotizacionController.getCotizacionPorFecha(startTime.toString());
        assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(DATES_FORMAT_WRONG.getValue());
    }

    @Test
    public void getCotizacionPorFechaErrorTest(){
        final ResponseEntity<?> result = bitcoinCotizacionController.getCotizacionPorFecha(buildStringTimestamp(startTime.plusSeconds(50)));
        assertThat(result.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(VALUE_NOT_FOUND.getValue());
    }

    @Test
    public void getCotizacionPromedioEntreFechaTest(){
        final ResponseEntity<?> result = bitcoinCotizacionController.getPromedioCotizacionEntreFechas(buildStringTimestamp(startTime), buildStringTimestamp(startTime.plusSeconds(45)));
        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isEqualTo(4.5d + 1);
    }

    @Test
    public void getCotizacionPromedioEntreFechaErrorTest(){
        final ResponseEntity<?> result = bitcoinCotizacionController.getPromedioCotizacionEntreFechas(buildStringTimestamp(startTime.plusSeconds(50)), buildStringTimestamp(startTime.plusSeconds(100)));
        assertThat(result.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(VALUE_NOT_FOUND);
    }

    @Test
    public void getCotizacionPromedioEntreFechaErrorDatesTest(){
        final ResponseEntity<?> result = bitcoinCotizacionController.getPromedioCotizacionEntreFechas(buildStringTimestamp(startTime.plusSeconds(100)), buildStringTimestamp(startTime.plusSeconds(50)));
        assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(DATES_ARE_WRONG.getValue());
    }

    public void setUp(LocalDateTime now){
        for (int i = 0; i < 10; i++) {
            final BitcoinValue bitcoinValue = new BitcoinValue(now.plusSeconds(i*5), i+1);
            bitcoinValueDao.save(bitcoinValue);
        }
    }

    private String buildStringTimestamp(LocalDateTime now){
        return now.toString().replace("T", " ");
    }
}
