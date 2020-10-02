import React, {useState} from 'react';

import { NavLink } from 'react-router-dom';
import { BrowserRouter as Router, Route, Link ,Switch} from "react-router-dom";
import AaaS from './components/ARXaaS';
import './bootstrap.css';
import SimpleUSER from './components/Simple';

const App = props =>{
  

  const [fileName, setfileName] = useState('Anonymized')
 

  const endpointHandler = (e) => {
    if(e.target.value !== fileName){
      setfileName(e.target.value)
    }
  }

  let advance=(  <div >
    <AaaS fileName = {fileName}/>   </div>);

  let simple= (<div >   <SimpleUSER fileName = {fileName} />  </div>);
  const [typeUser, settypeUser] = useState(simple);
  let content = (
    
      <div className="App" >
      <nav className="navbar navbar-expand-lg navbar-dark bg-primary" >
      <a className="navbar-brand" href="/">BK Anonymization</a>
      <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span className="navbar-toggler-icon"></span>
      </button>
      <div className="collapse navbar-collapse" id="navbarNav">
        <ul className="navbar-nav">

        </ul>
      <div className="form-inline my-2 my-lg-0">
      <input className="form-control mr-sm-2" type="text" placeholder="Name File Anonymization...here" aria-label="File-Endpoint" defaultValue={fileName} onChange={endpointHandler}></input>
      </div>

      </div>
    </nav>

   

<select
    className="form-control"
    onChange={(e) => {
        if (e.target.value==="Scient") settypeUser(advance);
         else settypeUser(simple);

    }
    }
>
    <option defaultValue  value="simple">Fast Anonymization</option>
    <option  value="Scient">Advance Anonymization</option>

{/* <option  defaultValue value="Scient">Advance Anonymization</option>
<option   value="simple">Fast Anonymization</option> */}
    
</select>

      {typeUser}
    

      </div>
     
  )

  return content
}

export default App;

