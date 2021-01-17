import { baseURL } from "../utils/settings";
import { makeOptions, handleHttpErrors, fetcher } from "../utils/fetchUtils";

function userFacade() {
  const URL = baseURL + "api/admin/";

  const fetchTotalSearches = (action, setError) => {
    const options = makeOptions("GET", true);
    return fetcher(URL + "count" , options, action, setError);
  };

  return {
    fetchTotalSearches,
    
  };
}

const facade = userFacade();
export default facade;