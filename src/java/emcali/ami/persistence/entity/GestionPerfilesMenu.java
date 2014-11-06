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
@Table(name = "GESTION_PERFILES_MENU", catalog = "", schema = "EMCALI_AMI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GestionPerfilesMenu.findAll", query = "SELECT g FROM GestionPerfilesMenu g"),
    @NamedQuery(name = "GestionPerfilesMenu.findByIdPerfilesMenu", query = "SELECT g FROM GestionPerfilesMenu g WHERE g.idPerfilesMenu = :idPerfilesMenu")})
public class GestionPerfilesMenu implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_PERFILES_MENU")
    private Long idPerfilesMenu;
    @JoinColumn(name = "FK_GESTION_PERFILES", referencedColumnName = "ID_PERFILES")
    @ManyToOne(optional = false)
    private GestionPerfiles fkGestionPerfiles;
    @JoinColumn(name = "FK_GESTION_MENU", referencedColumnName = "ID_MENU")
    @ManyToOne(optional = false)
    private GestionMenu fkGestionMenu;

    public GestionPerfilesMenu() {
    }

    public GestionPerfilesMenu(Long idPerfilesMenu) {
        this.idPerfilesMenu = idPerfilesMenu;
    }

    public Long getIdPerfilesMenu() {
        return idPerfilesMenu;
    }

    public void setIdPerfilesMenu(Long idPerfilesMenu) {
        this.idPerfilesMenu = idPerfilesMenu;
    }

    public GestionPerfiles getFkGestionPerfiles() {
        return fkGestionPerfiles;
    }

    public void setFkGestionPerfiles(GestionPerfiles fkGestionPerfiles) {
        this.fkGestionPerfiles = fkGestionPerfiles;
    }

    public GestionMenu getFkGestionMenu() {
        return fkGestionMenu;
    }

    public void setFkGestionMenu(GestionMenu fkGestionMenu) {
        this.fkGestionMenu = fkGestionMenu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPerfilesMenu != null ? idPerfilesMenu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GestionPerfilesMenu)) {
            return false;
        }
        GestionPerfilesMenu other = (GestionPerfilesMenu) object;
        if ((this.idPerfilesMenu == null && other.idPerfilesMenu != null) || (this.idPerfilesMenu != null && !this.idPerfilesMenu.equals(other.idPerfilesMenu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "emcali.ami.persistence.entity.GestionPerfilesMenu[ idPerfilesMenu=" + idPerfilesMenu + " ]";
    }
    
}
