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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author julian
 */
@Entity
@Table(name = "COMERCIAL_CONTRATOS", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComercialContratos.findAll", query = "SELECT c FROM ComercialContratos c"),
    @NamedQuery(name = "ComercialContratos.findByIdContratos", query = "SELECT c FROM ComercialContratos c WHERE c.idContratos = :idContratos"),
    @NamedQuery(name = "ComercialContratos.findByNumeroContrato", query = "SELECT c FROM ComercialContratos c WHERE c.numeroContrato = :numeroContrato")})
public class ComercialContratos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CONTRATOS")
    private Long idContratos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMERO_CONTRATO")
    private long numeroContrato;
    @JoinColumn(name = "FK_COMERCIAL_CLIENTES", referencedColumnName = "ID_CLIENTES")
    @ManyToOne(optional = false)
    private ComercialClientes fkComercialClientes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkComercialContratos")
    private List<ComercialProductos> comercialProductosList;

    public ComercialContratos() {
    }

    public ComercialContratos(Long idContratos) {
        this.idContratos = idContratos;
    }

    public ComercialContratos(Long idContratos, long numeroContrato) {
        this.idContratos = idContratos;
        this.numeroContrato = numeroContrato;
    }

    public Long getIdContratos() {
        return idContratos;
    }

    public void setIdContratos(Long idContratos) {
        this.idContratos = idContratos;
    }

    public long getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(long numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public ComercialClientes getFkComercialClientes() {
        return fkComercialClientes;
    }

    public void setFkComercialClientes(ComercialClientes fkComercialClientes) {
        this.fkComercialClientes = fkComercialClientes;
    }

    @XmlTransient
    public List<ComercialProductos> getComercialProductosList() {
        return comercialProductosList;
    }

    public void setComercialProductosList(List<ComercialProductos> comercialProductosList) {
        this.comercialProductosList = comercialProductosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idContratos != null ? idContratos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComercialContratos)) {
            return false;
        }
        ComercialContratos other = (ComercialContratos) object;
        if ((this.idContratos == null && other.idContratos != null) || (this.idContratos != null && !this.idContratos.equals(other.idContratos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.ComercialContratos[ idContratos=" + idContratos + " ]";
    }
    
}
