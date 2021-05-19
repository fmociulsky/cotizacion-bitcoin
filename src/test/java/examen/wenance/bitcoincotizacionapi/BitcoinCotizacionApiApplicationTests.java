package examen.wenance.bitcoincotizacionapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import examen.wenance.bitcoincotizacionapi.dao.BitcoinValueDao;
import examen.wenance.bitcoincotizacionapi.model.BitcoinAverageInfo;
import examen.wenance.bitcoincotizacionapi.model.BitcoinValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static examen.wenance.bitcoincotizacionapi.controller.ControllerMessages.DATES_ARE_WRONG;
import static examen.wenance.bitcoincotizacionapi.controller.ControllerMessages.DATE_FORMAT_WRONG;
import static examen.wenance.bitcoincotizacionapi.controller.ControllerMessages.VALUE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(classes = BitcoinCotizacionApiApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BitcoinCotizacionApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	BitcoinValueDao bitcoinValueDao;

	List<String> fechas;

	private static LocalDateTime startTime;

	@Before
	public void setUp(){
		fechas = new ArrayList<String>();
		startTime = LocalDateTime.now();
		setUp(startTime);
	}

	@Test
	public void getFechasTest() throws Exception {
		final MvcResult result = mockMvc.perform(get("/cotizaciones/registro")).andReturn();
		assertThat(result.getResponse().getStatus()).isEqualTo(OK.value());
		final ArrayList<String> fechasList = objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);
		assertThat(fechasList.size()).isEqualTo(10);
		assertThat(fechasList).isEqualTo(fechas);
	}

	@Test
	public void getCotizacionPorFechaOkTest() throws Exception {
		final MvcResult result = mockMvc.perform(get("/cotizaciones").param("fecha_exacta", fechas.get(0))).andReturn();
		assertThat(result.getResponse().getStatus()).isEqualTo(OK.value());
		assertThat(Double.valueOf(result.getResponse().getContentAsString())).isEqualTo(1d);
	}

	@Test
	public void getCotizacionPorFechaFormatErrorTest() throws Exception {
		final MvcResult result = mockMvc.perform(get("/cotizaciones").param("fecha_exacta", "asdfg")).andReturn();
		assertThat(result.getResponse().getStatus()).isEqualTo(BAD_REQUEST.value());
		assertThat(result.getResponse().getContentAsString()).isEqualTo(DATE_FORMAT_WRONG.getValue());
	}

	@Test
	public void getCotizacionPorFechaNotFoundErrorTest() throws Exception {
		final MvcResult result = mockMvc.perform(get("/cotizaciones").param("fecha_exacta", formatDate(LocalDateTime.now().plusSeconds(100)))).andReturn();
		assertThat(result.getResponse().getStatus()).isEqualTo(NOT_FOUND.value());
		assertThat(result.getResponse().getContentAsString()).isEqualTo(VALUE_NOT_FOUND.getValue());
	}

	@Test
	public void getPromedioCotizacionFechasOkTest() throws Exception {
		final MvcResult result = mockMvc.perform(get("/cotizaciones/promedio")
				.param("fecha_desde", fechas.get(0))
				.param("fecha_hasta", fechas.get(9)))
				.andReturn();
		assertThat(result.getResponse().getStatus()).isEqualTo(OK.value());
		final BitcoinAverageInfo bitcoinAverageInfo = objectMapper.readValue(result.getResponse().getContentAsString(), BitcoinAverageInfo.class);
		assertThat(bitcoinAverageInfo.getAverage()).isEqualTo(5.5d);
		assertThat(bitcoinAverageInfo.getPercent()).isEqualTo(81.82d);

	}

	@Test
	public void getPromedioCotizacionFechasFormatErrorTest() throws Exception {
		final MvcResult result = mockMvc.perform(get("/cotizaciones/promedio")
				.param("fecha_desde", fechas.get(0))
				.param("fecha_hasta", "asdasd"))
				.andReturn();
		assertThat(result.getResponse().getStatus()).isEqualTo(BAD_REQUEST.value());
		assertThat(result.getResponse().getContentAsString()).isEqualTo(DATE_FORMAT_WRONG.getValue());
	}

	@Test
	public void getPromedioCotizacionFechasWrongDatesErrorTest() throws Exception {
		final MvcResult result = mockMvc.perform(get("/cotizaciones/promedio")
				.param("fecha_desde", fechas.get(9))
				.param("fecha_hasta", fechas.get(0)))
				.andReturn();
		assertThat(result.getResponse().getStatus()).isEqualTo(BAD_REQUEST.value());
		assertThat(result.getResponse().getContentAsString()).isEqualTo(DATES_ARE_WRONG.getValue());
	}

	@Test
	public void getPromedioCotizacionFechasNotFoundErrorTest() throws Exception {
		final LocalDateTime fechaDesde = LocalDateTime.now().plusSeconds(100);
		final LocalDateTime fechaHasta = LocalDateTime.now().plusSeconds(150);
		final MvcResult result = mockMvc.perform(get("/cotizaciones/promedio")
				.param("fecha_desde", formatDate(fechaDesde))
				.param("fecha_hasta", formatDate(fechaHasta)))
				.andReturn();
		assertThat(result.getResponse().getStatus()).isEqualTo(NOT_FOUND.value());
		assertThat(result.getResponse().getContentAsString()).isEqualTo(VALUE_NOT_FOUND.getValue());
	}

	private void setUp(LocalDateTime now){
		for (int i = 0; i < 10; i++) {
			final BitcoinValue bitcoinValue = new BitcoinValue(now.plusSeconds(i*5), i+1);
			fechas.add(formatDate(now.plusSeconds(i*5)));
			bitcoinValueDao.save(bitcoinValue);
		}
	}

	private String formatDate(LocalDateTime date){
		return date.toString().replace("T", " ");
	}

}
