/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author marcg
 */
@Entity
@Table(name = "breeds")
public class Breed implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "breed", length = 50)
    private String breed;
    
    @Lob
    @Column(name = "info", length = 50000 )
    private String info;
    
    @ManyToMany(mappedBy = "breeds")
    private List<Searches> searches = new ArrayList();

    @OneToMany(mappedBy = "breed", cascade = CascadeType.PERSIST)
    private List<Dog> dog;
    
    public Breed() {
    }
    
    public void addDog(Dog dog){
        this.dog.add(dog);
        dog.addBreed(this);
    }
    
    public String getBreed() {
        return breed;
    }

    public String getInfo() {
        return info;
    }

    public Breed(String breed, String info) {
        this.breed = breed;
        this.info = info;
    }
    
    public void addSearch(Searches searches){
        this.searches.add(searches);
    }

    @Override
    public String toString() {
        return "Breed{" + "breed=" + breed + ", info=" + info + '}';
    }
    
    
    
}
