package examen.wenance.bitcoincotizacionapi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "BITCOIN_VALUE")
public class BitcoinValue implements Serializable {

    private static final long serialVersionUID = -1200037360762283831L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private LocalDateTime fechaCotizacion;
    private double cotizacion;

    public BitcoinValue() {
    }

    public BitcoinValue(LocalDateTime fechaCotizacion, double cotizacion) {
        this.fechaCotizacion = fechaCotizacion;
        this.cotizacion = cotizacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFechaCotizacion() {
        return fechaCotizacion;
    }

    public void setFechaCotizacion(LocalDateTime fechaCotizacion) {
        this.fechaCotizacion = fechaCotizacion;
    }

    public double getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(double cotizacion) {
        this.cotizacion = cotizacion;
    }
}
