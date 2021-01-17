/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOs;

import entities.Dog;
import facades.DateFacade;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marcg
 */
public class DogsDTO {

    public static final DateFacade dateFacade = DateFacade.getDateFacade("dd-MM-yyyy HH:mm:ss");

    private ArrayList<DogDTO> dogsDTO = new ArrayList();

    public DogsDTO(List<Dog> dogs) {
        for (Dog dog : dogs) {
            dogsDTO.add(new DogDTO(dog));
        }
    }

    public ArrayList<DogDTO> getUsersDTO() {
        return dogsDTO;
    }
}
