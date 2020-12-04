import React, {useEffect, useState} from 'react';
import AaaS from './components/ARXaaS';
import './bootstrap.css';
import SimpleUSER from './components/Simple';
import {Button, Modal} from "react-bootstrap";

const App = props => {


    const [fileName, setfileName] = useState('Anonymized')
    const [datasetUrl, setDatasetUrl] = useState('');
    const [showDatasetNoti, setShowDatasetNoti] = useState(false);

    useEffect(() => {
        const ws = new WebSocket("ws://localhost:5011");
        ws.onmessage = (event) => {
            console.log(event.data);

            try {
                const json = JSON.parse(event.data);
                console.log(json);
                setDatasetUrl(json.datasetUrl);
                setShowDatasetNoti(true);
            } catch (e) {
                console.log(e);
            }
        }
    }, [])


    const endpointHandler = (e) => {
        if (e.target.value !== fileName) {
            setfileName(e.target.value)
        }
    }

    let advance = (<div>
        <AaaS fileName={fileName}/></div>);

    let simple = (<div><SimpleUSER fileName={fileName}/></div>);
    const [typeUser, settypeUser] = useState("simple");

    const currentComponent = (typeUser === "simple" ? simple: advance);

    const handleCloseDatasetModal = () => setShowDatasetNoti(false);
    const modal = (
        <Modal show={showDatasetNoti} onHide={handleCloseDatasetModal}>
            <Modal.Header closeButton>
                <Modal.Title>Dataset is anonymized!</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>
                    A new anonymized dataset is created on CKAN
                </p>
                <p>
                    <a href={datasetUrl}>{datasetUrl}</a>
                </p>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleCloseDatasetModal}>
                    Close
                </Button>
            </Modal.Footer>
        </Modal>
    );

    let content = (

        <div className="App">
            <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
                <a className="navbar-brand" href="/">BK Anonymization</a>
                <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                        aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav">

                    </ul>
                    <div className="form-inline my-2 my-lg-0">
                        <input className="form-control mr-sm-2" type="text" placeholder="Name File Anonymization...here"
                               aria-label="File-Endpoint" defaultValue={fileName} onChange={endpointHandler}></input>
                    </div>

                </div>
            </nav>


            <select
                className="form-control"
                onChange={(e) => {
                    if (e.target.value === "Scient") settypeUser("advance");
                    else settypeUser("simple");

                }
                }
            >
                <option defaultValue value="simple">Fast Anonymization</option>
                <option value="Scient">Advance Anonymization</option>

                {/* <option  defaultValue value="Scient">Advance Anonymization</option>
<option   value="simple">Fast Anonymization</option> */}

            </select>

            {currentComponent}

            {modal}
        </div>

    )

    return content
}

export default App;

