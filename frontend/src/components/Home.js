import React, { useState, useEffect } from "react";
import { ListGroup } from "react-bootstrap";
import facade from "../facades/BreedFacade";

export default function Home({ setError }) {
  const [breeds, setBreeds] = useState([]);

  useEffect(() => {
    facade.fetchAllBreeds((data) => setBreeds([...data.dogs]), setError);
  }, []);

  return (
    <div className="text-center w-100">
      <h1>HEJSA</h1>
      <ListGroup>
        {breeds.map((breed) => {
          return <ListGroup.Item action >{breed.breed}</ListGroup.Item>;
        })}
      </ListGroup>
    </div>
  );
}
