import React, { useState } from 'react';
import Attribute from './Attribute'
import papaparse from 'papaparse';
import Result from './Result';
import PrivacyModelManager from './PrivacyModelManager';
import RenderPrivacyModels from './RenderPrivacyModels'
import RenderSuppressionLimit from "./RenderSuppressionLimit";
import requestAnony from '../dataExample/requestAnony.json';



const Anonymise = props => {

  var datarequestAnony  = JSON.stringify(requestAnony);

  const [model, setModel] = useState("");
  const { fileName } = props;
  const [currentData, setData] = useState("");
  const [attributes, setAttributes] = useState([]);
  const [privacyModels, setPrivacyModels] = useState([]);
  const [suppressionLimit, setSuppressionLimit] = useState(null);
  const [arxResp, setArxResp] = useState('');
  const [action, setAction] = useState('none');
  const attributeTypeModel = 'INSENSITIVE';

  const onFilesChange = file => {
    console.log(typeof file);
    papaparse.parse(file, {
      delimiter: ";",
  	  skipEmptyLines: true,
      complete: function (results) {
        if (results.data.length > 0) {
          let headers = results.data[0];
          setAttributes(headers.map(field => ({ field, attributeTypeModel })));
          setData(results.data)
        }
      }
    });
  };

  const handleTypeSelect = ({ target }, field, index) => {
    const { value: selectedType } = target;
    attributes[index] = {
      ...attributes[index],
      field,
      attributeTypeModel: selectedType
    };
    setAttributes(attributes);
  };

  const handlePrivacyAdd = (model) => {
    setPrivacyModels([...privacyModels,model]);
  };

  const handlePrivacyRemove = (index) => {
    let models = [];
    privacyModels.forEach((element, i) => {
      console.log(i, element);
      if(i !== index){
        models.push(element);
        console.log("Pushing because:", i, index, element)
      }else{
        console.log("Not pushing because:", i, index, element)
      }
    });

    console.log("Models: ",models);
    setPrivacyModels(models);
  };

  const handleSuppressionLimitAdd = (limit) => {
    setSuppressionLimit(limit);
  };

  const handleSuppressionLimitRemove = () => {
    setSuppressionLimit(null)
  }

  const handleHierarchyUpload = (file, field, index) => {
    papaparse.parse(file, {
      delimiter: ";",
  	  skipEmptyLines: true,
      complete: function (hierarchy) {
        attributes[index] = {
          ...attributes[index],
          hierarchy: hierarchy.data
        };
        setAttributes(attributes)
      }
    });
  };

  const handleRequest = (e, service) => {
    const payload = buildPayload();
    request(payload, service);
    //setAction(service);
  };

  const buildPayload = () => {
    let jsonModel = {};
    jsonModel['data'] = currentData;
    jsonModel['attributes'] = attributes;
    jsonModel['privacyModels'] = privacyModels;
    jsonModel["suppressionLimit"] = suppressionLimit;
    //jsonModel["title"]="tap du lieu demo";
    jsonModel["title"]=fileName;
    return jsonModel
  };


  const request = (payload, service) => {
    const requestOptions ={
      crossDomain: true,
      method: 'post',
      body: JSON.stringify(payload),

      headers: {
        "Content-Type": "application/json"
      }
    };
  fetch('http://localhost:5010/api/'+service,requestOptions)
  .then(res => res.json())
  .then(function (data) {
  setModel( JSON.stringify(data));
   // setArxResp(JSON.parse(JSON.stringify(data)));
    //setArxResp(data);
  })
  .catch((error) => console.log(error));;
};


const handlesendJson= (e, service) => {
  const payload = buildPayloadsendJson(service);
  requestsendJson(payload, service);
  //setAction(service);
};

const buildPayloadsendJson = (service) => {
  return datarequestAnony;

};

const requestsendJson = (payload, service) => {
  const requestOptions ={
    crossDomain: true,
    method: 'post',
    body: payload,
    headers: {
      "Content-Type": "application/json"
    }
  };
fetch('http://localhost:5010/api/'+service,requestOptions)
.then(res => res.json())
.then(function (data) {
  setModel(JSON.stringify(data));
  //return setModel( JSON.stringify(data));
  //setArxResp(data)
})
.catch((error) => console.log(error));;
};


const handlesendResult= (e, service) => {
const payload = buildPayloadsendResult(service);
requestsendResult(payload, service);
//setAction(service);
};

const buildPayloadsendResult = (service) => {
// if (service === 'sendResultAnalyze') return datasendResultAnalyze;
// else return datasendResultAnony;
let jsonModel={};
jsonModel['requestId']= "originalRequestId";
jsonModel['datasetUrl']= "http://ckan.localhost/dataset/abc-101010";
return jsonModel

};

const requestsendResult = (payload, service) => {
const requestOptions ={
  crossDomain: true,
  method: 'post',
  body: JSON.stringify(payload),
  headers: {
    "Content-Type": "application/json"
  }
};
fetch('http://localhost:5010/api/'+service,requestOptions)
.then(res => res.json())
.then(function (data) {
return 1;
//return setModel( JSON.stringify(data));
//setArxResp(data)
})
.catch((error) => console.log(error));;
};


const handlegetResult= (e, service) => {
const payload = buildPayloadgetResult(service);
requestgetResult(payload, service);
//setAction('analyze');
};

const buildPayloadgetResult = (service) => {
let jsonModel = {};
  jsonModel["suppressionLimit"] = suppressionLimit;
  return jsonModel
};

const  requestgetResult= (payload, service) => {
const requestOptions ={
crossDomain: true,
method: 'post',
body: JSON.stringify(payload),

headers: {
  "Content-Type": "application/json"
}
};
fetch('http://localhost:5010/api/'+service,requestOptions)
.then(res => res.json())
.then(function (data) {
setModel(JSON.stringify(data));
})
.catch((error) => console.log(error));;
};


  let content = (
    <div align="center">

         {/* <div></div>
         <div className="row">
            <div className="col-sm-2">
            <label>
                    <span>Option A</span>
                    <input type="radio" name="myRadioInput" value="Option A"/>
                </label>
            </div>
            <div className="col-sm">
            <label>
                    <span>Option B</span>
                    <input type="radio" name="myRadioInput" value="Option B"/>
                </label>
          
            </div>
          </div>
                 */}
                

      <div className="card border-primary mb-3" align="center" style={{ maxWidth: '40rem' }}>
        <div className="card-header">Upload</div>
        <div className="card-body">
          <div className="row">
            <div className="col-sm">
              <p className="card-text">Upload CSV formated file:</p>
            </div>
            <div className="col-sm">
              <input type='file'
                     id='file'
                     className='input-file'
                     accept='.csv'
                     onChange={e => onFilesChange(e.target.files[0])}
              />
            </div>
          </div>
        </div>
      </div>



      <div align='center'>
        <table>
          {attributes.map(({ field }, index) =>
            (<Attribute
              name={field}
              key={field}
              index={index}
              handleTypeSelect={handleTypeSelect}
              handleHierarchyUpload={handleHierarchyUpload}
            />))}
        </table>
      </div>


      <div className="card border-primary mb-3" style={{ maxWidth: '30rem' }}>
        <div className="card-header">Privacy model</div>
        <div className="card-body">


          <PrivacyModelManager
          privacyModels={privacyModels}
          handlePrivacyAdd={handlePrivacyAdd}
          handlePrivacyRemove={handlePrivacyRemove}
        />

        <RenderPrivacyModels
          privacyModels={privacyModels}
          handlePrivacyRemove={handlePrivacyRemove}
        />
        </div>
      </div>

      <div className="card border-primary mb-3" style={{ maxWidth: '20rem' }}>
        <div className="card-header">Suppression limit</div>
        <div className="card-body">

          <RenderSuppressionLimit
              suppressionLimit = {suppressionLimit}
              handleSuppressionLimitAdd = {handleSuppressionLimitAdd}
              handleSuppressionLimitRemove = {handleSuppressionLimitRemove}
          />

        </div>
      </div>

      <button className="btn btn-primary" onClick={(e) => handleRequest(e, 'anonymize')}>
            Anonymize
            </button>

              <button className="btn btn-primary" onClick={(e) => handlesendJson(e, 'sendJsonAnony')}>
            sendJsonAnony
              </button>


              <button className="btn btn-primary" onClick={(e) => handlesendResult(e, 'sendResultAnony')}>
            sendResult
              </button>

              <button className="btn btn-primary" onClick={(e) => handlegetResult(e, 'getResultAnony')}>
            getResult
              </button>

              <div>{model}</div>


      <Result
        arxResp={arxResp}
        action={action}
      />

      <br />

    </div>
  );

  return content;
};
export default Anonymise
