import React, { useState, useEffect } from "react";
import { Jumbotron, Row, Col, Form, Table } from "react-bootstrap";
import facade from "../facades/Dogfacade";
import SelectSearch from "react-select-search";
import breedFacade from "../facades/BreedFacade";
import "./Style.css";

export default function Dogs({ setError }) {
  const init = { name: "", dateOfBirth: "",  breed: "" };
  const [dog, setDog] = useState({ ...init });
  const [dogs, setDogs] = useState([]);
  const [breeds, setBreeds] = useState([]);

  const getUsersDogs = () => {
    facade.fetchAllOfAUSersDog((data) => {
      setDogs([...data.dogsDTO]);
    }, setError);
  };

  useEffect(() => {
    getUsersDogs();
    breedFacade.fetchAllBreeds((data) => setBreeds([...data.dogs]), setError);
  }, []);

  const onChange = (evt) => {
    setDog({
      ...dog,
      [evt.target.id]: evt.target.value,
    });
  };
  const onChangeBreed = (evt) => {
    setDog({
      ...dog,
      ["breed"]: evt,
    });
  };

  const onSubmit = () => {
    if (
      dog.name !== "" &&
      dog.dateOfBirth !== "" &&
      dog.breed !== ""
    ) {
      let tempDog = { ...dog };
      let date = dog.dateOfBirth.split("-").reverse().join("-");
      tempDog = {
        ...tempDog,
        ["dateOfBirth"]: date + " 12:00:00",
      };
      console.log(tempDog);
      facade.fetchAddDog(
        (data) => {
          getUsersDogs();
        },
        tempDog,
        setError
      );
    }

    //
  };

  return (
    <Row>
      <Col xs="8">
        <Jumbotron className="text-center w-100">
          <Form.Group
            controlId="formBasicEmail"
            onChange={onChange}
            onKeyPress={(evt) => {
              if (evt.charCode === 13) onSubmit();
            }}
          >
            <Form.Label className="float-left">Name</Form.Label>
            <Form.Control id="name" type="name" placeholder="Enter name" />

            <Form.Label className="float-left">Birthday</Form.Label>
            <Form.Control id="dateOfBirth" type="date" />

            <div className="mt-4">
              <SelectSearch
                options={breeds.map((breed) => {
                  return { name: breed.breed, value: breed.breed };
                })}
                search
                name="language"
                placeholder="Choose a breed"
                onChange={onChangeBreed}
              />
</div>
            <button className="btn btn-primary m-2" onClick={onSubmit}>
              Add Dog
            </button>
          </Form.Group>
        </Jumbotron>
      </Col>
      <Col xs="4">
        <Table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Birthday</th>
              <th>Info</th>
              <th>Breed</th>
            </tr>
          </thead>
          <tbody>
            {dogs.map((d) => {
              return (
                <tr>
                  <td>{d.name}</td>
                  <td>{d.DateOfBirth}</td>
                  <td>{d.info}</td>
                  <td>{d.breed}</td>
                </tr>
              );
            })}
          </tbody>
        </Table>
      </Col>
    </Row>
  );
}
