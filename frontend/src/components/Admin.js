import React, { useState, useEffect } from "react";
import { Jumbotron, ListGroup } from "react-bootstrap";
import facade from "../facades/AdminFacade";
import breedFacade from "../facades/BreedFacade";
import SelectSearch from "react-select-search";
import "./Style.css";

export default function Admin({ setError }) {
    const init = {count: "", breed: ""}

  const [totalSearches, setTotalSearches] = useState("");
  const [totalSearchesBreed, setTotalSearchesBreed] = useState({...init});
  const [breeds, setBreeds] = useState([]);

  useEffect(() => {
    facade.fetchTotalSearches((data) => setTotalSearches(data), setError);
    breedFacade.fetchAllBreeds((data) => setBreeds([...data.dogs]), setError);
  }, []);

  const onChange = (breed) => {
    facade.fetchTotalSearchesForABreed((data) => {
        setTotalSearchesBreed({count: data, breed})
    },breed, setError)
  }
  return (
    <div className="text-center w-100">
      <h1>Total searches: {totalSearches}</h1>
      {totalSearchesBreed.breed &&  <h1>Searches for {totalSearchesBreed.breed}: {totalSearchesBreed.count}</h1>}
     

      <div style={{alignSelf: "center"}}>
        <SelectSearch
          options={breeds.map((breed) => {
            return { name: breed.breed, value: breed.breed };
          })}
          search
          name="language"
          placeholder="Choose a breed"
          onChange={onChange}
        />
      </div>
    </div>
  );
}
