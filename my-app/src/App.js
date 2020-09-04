import React,{useState} from 'react';
import './App.css';
import Anonymise from './components/ARXaaS';

function App() {
  const [endpoint, setEndpoint] = useState('http://localhost:5000')

  const endpointHandler = (e) => {
    if(e.target.value !== endpoint){
      setEndpoint(e.target.value)
    }
  }
  return (
    
  
    <div className="App">
      <link
  rel="stylesheet"
  href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
  integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
  crossOrigin="anonymous"
/>;
<nav className="navbar navbar-expand-lg navbar-dark bg-primary" >
      <a className="navbar-brand" href="/">BK Anonymous</a>
      <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span className="navbar-toggler-icon"></span>
      </button>
      <div className="collapse navbar-collapse" id="navbarNav">
        <ul className="navbar-nav">
          
        </ul>
      <div className="form-inline my-2 my-lg-0">
      <input className="form-control mr-sm-2" type="text" placeholder="IP Server" aria-label="IP Server" defaultValue={endpoint} onChange={endpointHandler}></input>
      </div>
        
      </div>
    </nav>


      <div >
        <div className="row">
          <div className="col-lg-12 text-center">
            <h1 className="mt-5">BK Anonymous</h1>
            <Anonymise endpoint = {endpoint}/>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
