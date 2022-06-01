import axios from "axios";
import { useEffect, useState } from "react";

const App = () => {
  
  const [data, setData] = useState(undefined);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    const result = await axios.get("http://localhost:8080/users");
    
    setData(result.data);
  };

  const onUserAdded = event => {
    event.preventDefault();

    axios.post("http://localhost:8080/users/add", {
      name: event.target[0].value
    }).then(() => {
      event.target[0].value = "";
      fetchData();
    });
  };

  const onUserDeleted = id => {
    axios.delete(`http://localhost:8080/users/delete?id=${id}`)
      .then(() => {
        fetchData();
      });
  };

  return (
    <div className="container">
      <h1 className="display-3 text-center">Spring REST Application</h1>

      <hr />
      
      <h3 className="display-6">Add a new user</h3>
      <form onSubmit={onUserAdded}>
        <div className="input-group mb-3">
          <input type="text" className="form-control" placeholder="New User Name" aria-label="New User Name" aria-describedby="button-addon2" />
          <button className="btn btn-success" type="submit" id="button-addon2">Add</button>
        </div>
      </form>
      
      <hr />

      <div className="d-grid gap-2 col-6 mx-auto">
        <ul className="list-group">
          {data === undefined && <div className="spinner-border mx-auto" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>}
          {(data !== undefined && data.length === 0) && <h4 className="display-6">No Users Found</h4>}
          {(data !== undefined && data.length > 0) && data.map(item => {
              return <li className="list-group-item" key={item._id}>
                <div className="input-group mb-3">
                  <input type="text" readOnly className="form-control" value={item.name} />
                  <button className="btn btn-danger" onClick={() => onUserDeleted(item._id)}>Delete</button>
                </div>
              </li>;})}
        </ul>
      </div>
    </div>
  );
};

export default App;
