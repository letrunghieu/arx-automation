import React, {useState} from 'react';


import AaaS from './components/ARXaaS';
import './bootstrap.css'

const App = props =>{

  const [fileName, setfileName] = useState('')

  const endpointHandler = (e) => {
    if(e.target.value !== fileName){
      setfileName(e.target.value)
    }
  }
    
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


      <div >
        <div className="row">
          <div className="col-lg-12 text-center">
            <h1 className="mt-5">BK Anonymization</h1>
            <AaaS fileName = {fileName}/>
          </div>
        </div>
      </div>


      </div>
  )

  return content
}

export default App;

