/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOs;

import entities.Dog;
import java.util.Date;

/**
 *
 * @author marcg
 */
public class DogDTO {

    private String name;
    private Date DateOfBirth;
    private String info;
    private String breed;

    public DogDTO(Dog dog) {
        this.name = dog.getName();
        this.DateOfBirth = dog.getDateOfBirth();
        this.info = dog.getInfo();
        this.breed = dog.getBreed();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(Date DateOfBirth) {
        this.DateOfBirth = DateOfBirth;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }
    
    
    


}
