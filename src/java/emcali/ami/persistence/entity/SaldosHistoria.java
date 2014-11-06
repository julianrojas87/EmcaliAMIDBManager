/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author julian
 */
@Entity
@Table(name = "SALDOS_HISTORIA", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SaldosHistoria.findAll", query = "SELECT s FROM SaldosHistoria s"),
    @NamedQuery(name = "SaldosHistoria.findByIdSaldosHistoria", query = "SELECT s FROM SaldosHistoria s WHERE s.idSaldosHistoria = :idSaldosHistoria"),
    @NamedQuery(name = "SaldosHistoria.findByFechaInicioCiclo", query = "SELECT s FROM SaldosHistoria s WHERE s.fechaInicioCiclo = :fechaInicioCiclo"),
    @NamedQuery(name = "SaldosHistoria.findByFechaLectura", query = "SELECT s FROM SaldosHistoria s WHERE s.fechaLectura = :fechaLectura"),
    @NamedQuery(name = "SaldosHistoria.findByLecturaInicio", query = "SELECT s FROM SaldosHistoria s WHERE s.lecturaInicio = :lecturaInicio"),
    @NamedQuery(name = "SaldosHistoria.findByLecturaActual", query = "SELECT s FROM SaldosHistoria s WHERE s.lecturaActual = :lecturaActual"),
    @NamedQuery(name = "SaldosHistoria.findBySaldoInicial", query = "SELECT s FROM SaldosHistoria s WHERE s.saldoInicial = :saldoInicial"),
    @NamedQuery(name = "SaldosHistoria.findBySumaRecargas", query = "SELECT s FROM SaldosHistoria s WHERE s.sumaRecargas = :sumaRecargas"),
    @NamedQuery(name = "SaldosHistoria.findBySaldoActual", query = "SELECT s FROM SaldosHistoria s WHERE s.saldoActual = :saldoActual"),
    @NamedQuery(name = "SaldosHistoria.findByConsumoPeriodo", query = "SELECT s FROM SaldosHistoria s WHERE s.consumoPeriodo = :consumoPeriodo"),
    @NamedQuery(name = "SaldosHistoria.findByDiasPeriodo", query = "SELECT s FROM SaldosHistoria s WHERE s.diasPeriodo = :diasPeriodo"),
    @NamedQuery(name = "SaldosHistoria.findByDiasCiclo", query = "SELECT s FROM SaldosHistoria s WHERE s.diasCiclo = :diasCiclo")})
public class SaldosHistoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_SALDOS_HISTORIA")
    private Long idSaldosHistoria;
    @Column(name = "FECHA_INICIO_CICLO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicioCiclo;
    @Column(name = "FECHA_LECTURA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaLectura;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "LECTURA_INICIO")
    private Double lecturaInicio;
    @Column(name = "LECTURA_ACTUAL")
    private Double lecturaActual;
    @Column(name = "SALDO_INICIAL")
    private Double saldoInicial;
    @Column(name = "SUMA_RECARGAS")
    private Double sumaRecargas;
    @Column(name = "SALDO_ACTUAL")
    private Double saldoActual;
    @Column(name = "CONSUMO_PERIODO")
    private Double consumoPeriodo;
    @Column(name = "DIAS_PERIODO")
    private Double diasPeriodo;
    @Column(name = "DIAS_CICLO")
    private Double diasCiclo;
    @JoinColumn(name = "FK_SALDOS_TIPO_SALDO", referencedColumnName = "ID_TIPO_SALDO")
    @ManyToOne(optional = false)
    private SaldosTipoSaldo fkSaldosTipoSaldo;
    @JoinColumn(name = "FK_RECARGA_TIPO_RECARGA", referencedColumnName = "ID_TIPO_RECARGA")
    @ManyToOne(optional = false)
    private RecargaTipoRecarga fkRecargaTipoRecarga;
    @JoinColumn(name = "FK_COMERCIAL_PRODUCTOS", referencedColumnName = "ID_PRODUCTOS")
    @ManyToOne(optional = false)
    private ComercialProductos fkComercialProductos;

    public SaldosHistoria() {
    }

    public SaldosHistoria(Long idSaldosHistoria) {
        this.idSaldosHistoria = idSaldosHistoria;
    }

    public Long getIdSaldosHistoria() {
        return idSaldosHistoria;
    }

    public void setIdSaldosHistoria(Long idSaldosHistoria) {
        this.idSaldosHistoria = idSaldosHistoria;
    }

    public Date getFechaInicioCiclo() {
        return fechaInicioCiclo;
    }

    public void setFechaInicioCiclo(Date fechaInicioCiclo) {
        this.fechaInicioCiclo = fechaInicioCiclo;
    }

    public Date getFechaLectura() {
        return fechaLectura;
    }

    public void setFechaLectura(Date fechaLectura) {
        this.fechaLectura = fechaLectura;
    }

    public Double getLecturaInicio() {
        return lecturaInicio;
    }

    public void setLecturaInicio(Double lecturaInicio) {
        this.lecturaInicio = lecturaInicio;
    }

    public Double getLecturaActual() {
        return lecturaActual;
    }

    public void setLecturaActual(Double lecturaActual) {
        this.lecturaActual = lecturaActual;
    }

    public Double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(Double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public Double getSumaRecargas() {
        return sumaRecargas;
    }

    public void setSumaRecargas(Double sumaRecargas) {
        this.sumaRecargas = sumaRecargas;
    }

    public Double getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(Double saldoActual) {
        this.saldoActual = saldoActual;
    }

    public Double getConsumoPeriodo() {
        return consumoPeriodo;
    }

    public void setConsumoPeriodo(Double consumoPeriodo) {
        this.consumoPeriodo = consumoPeriodo;
    }

    public Double getDiasPeriodo() {
        return diasPeriodo;
    }

    public void setDiasPeriodo(Double diasPeriodo) {
        this.diasPeriodo = diasPeriodo;
    }

    public Double getDiasCiclo() {
        return diasCiclo;
    }

    public void setDiasCiclo(Double diasCiclo) {
        this.diasCiclo = diasCiclo;
    }

    public SaldosTipoSaldo getFkSaldosTipoSaldo() {
        return fkSaldosTipoSaldo;
    }

    public void setFkSaldosTipoSaldo(SaldosTipoSaldo fkSaldosTipoSaldo) {
        this.fkSaldosTipoSaldo = fkSaldosTipoSaldo;
    }

    public RecargaTipoRecarga getFkRecargaTipoRecarga() {
        return fkRecargaTipoRecarga;
    }

    public void setFkRecargaTipoRecarga(RecargaTipoRecarga fkRecargaTipoRecarga) {
        this.fkRecargaTipoRecarga = fkRecargaTipoRecarga;
    }

    public ComercialProductos getFkComercialProductos() {
        return fkComercialProductos;
    }

    public void setFkComercialProductos(ComercialProductos fkComercialProductos) {
        this.fkComercialProductos = fkComercialProductos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSaldosHistoria != null ? idSaldosHistoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaldosHistoria)) {
            return false;
        }
        SaldosHistoria other = (SaldosHistoria) object;
        if ((this.idSaldosHistoria == null && other.idSaldosHistoria != null) || (this.idSaldosHistoria != null && !this.idSaldosHistoria.equals(other.idSaldosHistoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.SaldosHistoria[ idSaldosHistoria=" + idSaldosHistoria + " ]";
    }
    
}
