/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.entity;

import java.io.Serializable;
import java.util.List;
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
@Table(name = "RECARGA_TIPO_RECARGA", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecargaTipoRecarga.findAll", query = "SELECT r FROM RecargaTipoRecarga r"),
    @NamedQuery(name = "RecargaTipoRecarga.findByIdTipoRecarga", query = "SELECT r FROM RecargaTipoRecarga r WHERE r.idTipoRecarga = :idTipoRecarga"),
    @NamedQuery(name = "RecargaTipoRecarga.findByTipoRecarga", query = "SELECT r FROM RecargaTipoRecarga r WHERE r.tipoRecarga = :tipoRecarga")})
public class RecargaTipoRecarga implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIPO_RECARGA")
    private Long idTipoRecarga;
    @Size(max = 45)
    @Column(name = "TIPO_RECARGA")
    private String tipoRecarga;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkPrepagoTipoRecarga")
    private List<RecargaRecargas> recargaRecargasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkRecargaTipoRecarga")
    private List<SaldosHistoria> saldosHistoriaList;

    public RecargaTipoRecarga() {
    }

    public RecargaTipoRecarga(Long idTipoRecarga) {
        this.idTipoRecarga = idTipoRecarga;
    }

    public Long getIdTipoRecarga() {
        return idTipoRecarga;
    }

    public void setIdTipoRecarga(Long idTipoRecarga) {
        this.idTipoRecarga = idTipoRecarga;
    }

    public String getTipoRecarga() {
        return tipoRecarga;
    }

    public void setTipoRecarga(String tipoRecarga) {
        this.tipoRecarga = tipoRecarga;
    }

    @XmlTransient
    public List<RecargaRecargas> getRecargaRecargasList() {
        return recargaRecargasList;
    }

    public void setRecargaRecargasList(List<RecargaRecargas> recargaRecargasList) {
        this.recargaRecargasList = recargaRecargasList;
    }

    @XmlTransient
    public List<SaldosHistoria> getSaldosHistoriaList() {
        return saldosHistoriaList;
    }

    public void setSaldosHistoriaList(List<SaldosHistoria> saldosHistoriaList) {
        this.saldosHistoriaList = saldosHistoriaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoRecarga != null ? idTipoRecarga.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecargaTipoRecarga)) {
            return false;
        }
        RecargaTipoRecarga other = (RecargaTipoRecarga) object;
        if ((this.idTipoRecarga == null && other.idTipoRecarga != null) || (this.idTipoRecarga != null && !this.idTipoRecarga.equals(other.idTipoRecarga))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.RecargaTipoRecarga[ idTipoRecarga=" + idTipoRecarga + " ]";
    }
    
}
