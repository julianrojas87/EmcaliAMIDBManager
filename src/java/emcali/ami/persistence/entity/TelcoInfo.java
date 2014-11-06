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
@Table(name = "TELCO_INFO", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TelcoInfo.findAll", query = "SELECT t FROM TelcoInfo t"),
    @NamedQuery(name = "TelcoInfo.findByIdSuscriptor", query = "SELECT t FROM TelcoInfo t WHERE t.idSuscriptor = :idSuscriptor")})
public class TelcoInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_SUSCRIPTOR")
    private Long idSuscriptor;
    @JoinColumn(name = "FK_COMERCIAL_CLIENTES", referencedColumnName = "ID_CLIENTES")
    @ManyToOne(optional = false)
    private ComercialClientes fkComercialClientes;
    @JoinColumn(name = "FK_AMY_MEDIDORES", referencedColumnName = "ID_MEDIDORES")
    @ManyToOne(optional = false)
    private AmyMedidores fkAmyMedidores;

    public TelcoInfo() {
    }

    public TelcoInfo(Long idSuscriptor) {
        this.idSuscriptor = idSuscriptor;
    }

    public Long getIdSuscriptor() {
        return idSuscriptor;
    }

    public void setIdSuscriptor(Long idSuscriptor) {
        this.idSuscriptor = idSuscriptor;
    }

    public ComercialClientes getFkComercialClientes() {
        return fkComercialClientes;
    }

    public void setFkComercialClientes(ComercialClientes fkComercialClientes) {
        this.fkComercialClientes = fkComercialClientes;
    }

    public AmyMedidores getFkAmyMedidores() {
        return fkAmyMedidores;
    }

    public void setFkAmyMedidores(AmyMedidores fkAmyMedidores) {
        this.fkAmyMedidores = fkAmyMedidores;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSuscriptor != null ? idSuscriptor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TelcoInfo)) {
            return false;
        }
        TelcoInfo other = (TelcoInfo) object;
        if ((this.idSuscriptor == null && other.idSuscriptor != null) || (this.idSuscriptor != null && !this.idSuscriptor.equals(other.idSuscriptor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.TelcoInfo[ idSuscriptor=" + idSuscriptor + " ]";
    }
    
}
