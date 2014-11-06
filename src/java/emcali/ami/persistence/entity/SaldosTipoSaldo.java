/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author julian
 */
@Entity
@Table(name = "SALDOS_TIPO_SALDO", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SaldosTipoSaldo.findAll", query = "SELECT s FROM SaldosTipoSaldo s"),
    @NamedQuery(name = "SaldosTipoSaldo.findByIdTipoSaldo", query = "SELECT s FROM SaldosTipoSaldo s WHERE s.idTipoSaldo = :idTipoSaldo"),
    @NamedQuery(name = "SaldosTipoSaldo.findByTipoSaldo", query = "SELECT s FROM SaldosTipoSaldo s WHERE s.tipoSaldo = :tipoSaldo")})
public class SaldosTipoSaldo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIPO_SALDO")
    private Long idTipoSaldo;
    @Size(max = 45)
    @Column(name = "TIPO_SALDO")
    private String tipoSaldo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkSaldosTipoSaldo")
    private Collection<SaldosHistoria> saldosHistoriaCollection;

    public SaldosTipoSaldo() {
    }

    public SaldosTipoSaldo(Long idTipoSaldo) {
        this.idTipoSaldo = idTipoSaldo;
    }

    public Long getIdTipoSaldo() {
        return idTipoSaldo;
    }

    public void setIdTipoSaldo(Long idTipoSaldo) {
        this.idTipoSaldo = idTipoSaldo;
    }

    public String getTipoSaldo() {
        return tipoSaldo;
    }

    public void setTipoSaldo(String tipoSaldo) {
        this.tipoSaldo = tipoSaldo;
    }

    @XmlTransient
    public Collection<SaldosHistoria> getSaldosHistoriaCollection() {
        return saldosHistoriaCollection;
    }

    public void setSaldosHistoriaCollection(Collection<SaldosHistoria> saldosHistoriaCollection) {
        this.saldosHistoriaCollection = saldosHistoriaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoSaldo != null ? idTipoSaldo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaldosTipoSaldo)) {
            return false;
        }
        SaldosTipoSaldo other = (SaldosTipoSaldo) object;
        if ((this.idTipoSaldo == null && other.idTipoSaldo != null) || (this.idTipoSaldo != null && !this.idTipoSaldo.equals(other.idTipoSaldo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.SaldosTipoSaldo[ idTipoSaldo=" + idTipoSaldo + " ]";
    }
    
}
