/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.persistence.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author julian
 */
@Entity
@Table(name = "CONSUMOS_CONSUMOS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConsumosConsumos.findAll", query = "SELECT c FROM ConsumosConsumos c"),
    @NamedQuery(name = "ConsumosConsumos.findByIdConsumo", query = "SELECT c FROM ConsumosConsumos c WHERE c.idConsumo = :idConsumo"),
    @NamedQuery(name = "ConsumosConsumos.findByValor", query = "SELECT c FROM ConsumosConsumos c WHERE c.valor = :valor")})
public class ConsumosConsumos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CONSUMO")
    private Long idConsumo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "VALOR")
    private Double valor;
    @JoinColumn(name = "FK_AMY_TIPOLECTURAS", referencedColumnName = "ID_TIPOLECTURA")
    @ManyToOne(optional = false)
    private AmyTipolecturas fkAmyTipolecturas;

    public ConsumosConsumos() {
    }

    public ConsumosConsumos(Long idConsumo) {
        this.idConsumo = idConsumo;
    }

    public Long getIdConsumo() {
        return idConsumo;
    }

    public void setIdConsumo(Long idConsumo) {
        this.idConsumo = idConsumo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public AmyTipolecturas getFkAmyTipolecturas() {
        return fkAmyTipolecturas;
    }

    public void setFkAmyTipolecturas(AmyTipolecturas fkAmyTipolecturas) {
        this.fkAmyTipolecturas = fkAmyTipolecturas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConsumo != null ? idConsumo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConsumosConsumos)) {
            return false;
        }
        ConsumosConsumos other = (ConsumosConsumos) object;
        if ((this.idConsumo == null && other.idConsumo != null) || (this.idConsumo != null && !this.idConsumo.equals(other.idConsumo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.ConsumosConsumos[ idConsumo=" + idConsumo + " ]";
    }
    
}
