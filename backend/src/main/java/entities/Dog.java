/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author marcg
 */
@Entity
@Table(name = "dog")
public class Dog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    private User user;

    @Column(name = "name", length = 50, nullable = false)
    private String name;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "birth_day", nullable = false)
    private Date DateOfBirth;

    @Column(name = "info", nullable = true)
    private String info;

    @Column(name = "breed", length = 50, nullable = false)
    private String breed;

    public Dog(String name, Date DateOfBirth, String info, String breed) {
        this.name = name;
        this.DateOfBirth = DateOfBirth;
        this.info = info;
        this.breed = breed;
    }

    public Dog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public Date getDateOfBirth() {
        return DateOfBirth;
    }

    public String getInfo() {
        return info;
    }

    public String getBreed() {
        return breed;
    }

    public void addUser(User user) {
        this.user = user;
    }

}
