/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

/**
 *
 * @author marcg
 */
@Entity
@Table(name = "searches")
public class Searches implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id")
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date date;
    
    @JoinTable(name = "searches_breeds", joinColumns = {
        @JoinColumn(name = "search_id", referencedColumnName = "search_id")}, inverseJoinColumns = {
        @JoinColumn(name = "breed", referencedColumnName = "breed")})
    @ManyToMany
    private List<Breed> breeds = new ArrayList();

    public Searches() {
    }

    public Searches(Date date) {
        this.date = date;
    }
    
    public void addBreed(Breed breed){
        this.breeds.add(breed);
        breed.addSearch(this);
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    
    
}
