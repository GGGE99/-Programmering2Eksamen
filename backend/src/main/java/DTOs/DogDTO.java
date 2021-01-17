/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOs;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import entities.Dog;
import errorhandling.DateException;
import errorhandling.InvalidInputException;
import java.util.Date;

/**
 *
 * @author marcg
 */
public class DogDTO {

    private long id;
    private String name;
    private Date DateOfBirth;
    private String info;
    private String breed;

    public DogDTO(Dog dog) {
        this.id = dog.getId();
        this.name = dog.getName();
        this.DateOfBirth = dog.getDateOfBirth();
        this.breed = dog.getBreed().getBreed();
        this.info = dog.getBreed().getInfo();
    }

    public DogDTO(String name, String breed) {
        this.name = name;
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
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

    public boolean testValidValues() throws InvalidInputException, DateException {
        if (name.length() < 2 || name.length() > 50) {
            throw new InvalidInputException("A dogs name should be between 2 and 50 charactors (" + name + " is " + name.length() + ")");
        } else if (DateOfBirth.after(new Date())) {
            throw new DateException(String.format("Your dog isn't born yet? Please selete a date before %s (You had the date %s)", new Date().toString(), DateOfBirth));
        } else if (breed == null) {
            throw new InvalidInputException("Please selete a Breed");
        }
        return true;
    }

}
