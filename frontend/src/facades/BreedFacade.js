import { baseURL } from "../utils/settings";
import { makeOptions, handleHttpErrors, fetcher } from "../utils/fetchUtils";

function userFacade() {
  const URL = baseURL + "api/dog-breed/";

  const fetchAllBreeds = (action, setError) => {
    const options = makeOptions("GET", true);
    return fetcher(URL , options, action, setError);
  };

  const fetchBreed = (action, breed, setError) => {
    const options = makeOptions("GET", true);
    return fetcher(URL + breed , options, action, setError);
  };

  return {
    fetchAllBreeds,
    fetchBreed,
  };
}

const facade = userFacade();
export default facade;