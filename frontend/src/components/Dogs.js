import React, { useState, useEffect } from "react";
import { Jumbotron, Row, Col, Form, Table } from "react-bootstrap";
import facade from "../facades/Dogfacade";
import SelectSearch from "react-select-search";
import breedFacade from "../facades/BreedFacade";
import "./Style.css";

export default function Dogs({ setError, error }) {
  const init = { name: "", DateOfBirth: "", breed: "" };
  const [dog, setDog] = useState({ ...init });
  const [dogs, setDogs] = useState([]);
  const [breeds, setBreeds] = useState([]);
  const [edit, setEdit] = useState(false);

  const getUsersDogs = () => {
    facade.fetchAllOfAUSersDog((data) => {
      setDogs([...data.dogsDTO]);
    }, setError);
  };

  useEffect(() => {
    getUsersDogs();
    breedFacade.fetchAllBreeds((data) => {
      setBreeds([...data.dogs]);
      setError("");
    }, setError);
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

  const submitUpdate = () => {
    facade.fetchUpdateDog(
      (data) => {
        setEdit(false);
        setError("");
        getUsersDogs();
        setDog({...init})
      },
      dog,
      setError
    );
  };

  const click = (d) => {
    setDog({ ...d });
    setEdit(true);
  };

  const onSubmit = () => {
    if (dog.name !== "" && dog.DateOfBirth !== "" && dog.breed !== "") {
      let tempDog = { ...dog };
      let date = dog.DateOfBirth.split("-").reverse().join("-");
      tempDog = {
        ...tempDog,
        ["DateOfBirth"]: date + " 12:00:00",
      };
      console.log(tempDog);
      facade.fetchAddDog(
        (data) => {
          getUsersDogs();
          setDog({...init})
        },
        tempDog,
        setError
      );
    }
  };

  const onClickDelete = (id) => {
    facade.fetchDeleteDog((data) => {getUsersDogs()}, id, setError);
  };

  return (
    <Row>
      <Col xs="7">
        <Jumbotron className="text-center w-100">
          <Form.Group
            controlId="formBasicEmail"
            onChange={onChange}
            onKeyPress={(evt) => {
              if (evt.charCode === 13) onSubmit();
            }}
          >
            <Form.Label className="float-left">Name</Form.Label>
            <Form.Control
              minLength="2"
              maxLength="50"
              id="name"
              type="name"
              value={dog.name}
              placeholder="Enter name"
            />

            <Form.Label className="float-left">Birthday</Form.Label>
            <Form.Control id="DateOfBirth" value={dog.DateOfBirth} type="date" />

            <div className="mt-4">
              <SelectSearch
                options={breeds.map((breed) => {
                  return { name: breed.breed, value: breed.breed };
                })}
                search
                name="language"
                placeholder="Choose a breed"
                onChange={onChangeBreed}
                value={dog.breed}
              />
            </div>
            {edit ? (
              <button className="btn btn-primary m-2" onClick={submitUpdate}>
                edit
              </button>
            ) : (
              <button className="btn btn-primary m-2" onClick={onSubmit}>
                Add Dog
              </button>
            )}
          </Form.Group>
          <p>{error}</p>
        </Jumbotron>
      </Col>
      <Col xs="5">
        <Table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Birthday</th>
              <th>Info</th>
              <th>Breed</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {dogs.map((d) => {
              return (
                <tr>
                  <td onClick={() => click(d)}>{d.name}</td>
                  <td onClick={() => click(d)}>{d.DateOfBirth}</td>
                  <td onClick={() => click(d)}>
                    {d.info.length > 25
                      ? d.info.substring(0, 25) + " . . ."
                      : d.info}
                  </td >
                  <td onClick={() => click(d)}>{d.breed}</td>
                  <td>
                    <button
                      className="btn btn-transparent"
                      onClick={() => onClickDelete(d.id)}
                    >
                      delete
                    </button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </Table>
      </Col>
    </Row>
  );
}
