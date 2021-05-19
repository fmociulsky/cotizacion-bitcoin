package examen.wenance.bitcoincotizacionapi.controller;


public enum ControllerMessages {
    VALUE_NOT_FOUND("No se encontro cotizacion para la fecha enviada"),
    DATE_FORMAT_WRONG("El formato de la fecha enviada es incorrecta: se espera el formato yyyy-mm-dd hh:mm:ss[.fffffffff]"),
    DATES_ARE_WRONG("La Fecha Desde debe ser menor o igual a la Fecha Hasta");

    private final String value;
    ControllerMessages(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
