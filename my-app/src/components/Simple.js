import React, { useState } from 'react';
import AttributeCheckBox from './AttributeCheckBox'
import papaparse from 'papaparse';
import PrivacyModelManager from './PrivacyModelManager';
import RenderPrivacyModels from './RenderPrivacyModels'
import RenderSuppressionLimit from "./RenderSuppressionLimit";
import requestAnony from '../dataExample/requestAnony.json';
import ClipLoader from "react-spinners/ClipLoader";


const Simple = props => {

  var datarequestAnony  = JSON.stringify(requestAnony);
  const defaultUser={ 
    privacyModel: "KANONYMITY",
    params: {k: "5"}
}

const[originalFileName,setoriginalFileName]=useState("");
  var temp=null;
  const [link,setLink]=useState("");
 // const [dialogRes,setdialogRes]=useState(null);
  //const [model, setModel] = useState("");
  const { fileName } = props;
  const [resultUrl, setresultUrl] = useState("");
  const [currentData, setData] = useState("");
  const [attributes, setAttributes] = useState([]);
  const [privacyModels, setPrivacyModels] = useState([ defaultUser]);
  const [suppressionLimit, setSuppressionLimit] = useState(0.99);
  //const [arxResp, setArxResp] = useState('');
  //const [action, setAction] = useState('none');
  const attributeTypeModel = 'QUASIIDENTIFYING';
  const [loadingAnony,setLoadingAnony]=useState(false);
  const [loadingResult,setLoadingResult]=useState(false);


  var renderWaitAnony=(<div className="sweet-loading">
  <ClipLoader
    //css={override}
    size={25}
    color={"#123abc"}
    loading={loadingAnony}
  />
</div>);
var renderWaitResult=(<div className="sweet-loading">
<ClipLoader
  //css={override}
  size={25}
  color={"#123abc"}
  loading={loadingResult}
/>
</div>);

  const onFilesChange = file => {
    //console.log(file.name);
    setoriginalFileName(file.name);
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
    jsonModel["originalfileName"]=originalFileName;
    return jsonModel
  };


  const request = (payload, service) => {
    setLoadingAnony(true);
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
    setLoadingAnony(false);
  //setModel( JSON.stringify(data));
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
  //setModel(JSON.stringify(data));
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




const  handlegetResult= (e,service) => {
setLoadingResult(true);
var jsonModel = {};
  //jsonModel["suppressionLimit"] = suppressionLimit;
const requestOptions ={
crossDomain: true,
method: 'post',
body: JSON.stringify(jsonModel),

headers: {
  "Content-Type": "application/json"
}
};
fetch('http://localhost:5010/api/'+service,requestOptions)
.then(res => res.json())
.then(function (data) {
  setLoadingResult(false);
  var objData=JSON.parse(JSON.stringify(data));
  handlegetLink(objData.datasetUrl);
      // console.log(objData.datasetUrl);
  //     //console.log(JSON.stringify(data).);
  //   //setrequestId(JSON.stringify(data).requestId);
  //   setresultUrl(objData.datasetUrl);
})
.catch((error) => console.log(error));;
};





// const  handlegetLink= () => {
//   const requestOptions ={
//   crossDomain: true,
//   method: 'get',
//   };
//   fetch('http://localhost:8082/http://localhost:5000/dataset/67ea57c0-6663-4789-9c35-56cfd6fb94ed/resource/0f85b001-283e-4edf-9494-81509874b5a3/download/test2.csv',requestOptions)
//   .then((response) => response.blob())
//   .then(function (data) {
//     temp=new File([data], "me.csv", { lastModified: new Date().getTime(), type: data.type });
//     console.log(typeof temp);
//     papaparse.parse(temp, {
//       delimiter: ";",
//   	  skipEmptyLines: true,
//       complete: function (results) {
//         if (results.data.length > 0) {
//           let headers = results.data[0];
//           setAttributes(headers.map(field => ({ field, attributeTypeModel })));
//           setData(results.data)
//         }
//       }
//     });
//     console.log(currentData);
//   })
//   .catch((error) => console.log(error));;
//   };
  






  const handlegetLink=(linkData)=>
  {
    
    setLink((<div>
      Xem kết quả ở link sau:
      <a href={linkData} target="_blank" style={{ color:"blue" }}> {linkData}</a>
    </div>))
  }





  // const handlegetResult= (e, service) => {
  //   const payload = buildPayloadgetResult(service);
  //   requestgetResult(payload, service);
  //   //setAction('analyze');
  //   };
    
  //   const buildPayloadgetResult = (service) => {
  //   let jsonModel = {};
  //     jsonModel["suppressionLimit"] = suppressionLimit;
  //     return jsonModel
  //   };
    
  //   const  requestgetResult= (payload, service) => {
  //   const requestOptions ={
  //   crossDomain: true,
  //   method: 'post',
  //   body: JSON.stringify(payload),
    
  //   headers: {
  //     "Content-Type": "application/json"
  //   }
  //   };
  //   fetch('http://localhost:5010/api/'+service,requestOptions)
  //   .then(res => res.json())
  //   .then(function (data) {
  //     var objData=JSON.parse(JSON.stringify(data));
  //     console.log(objData.datasetUrl);
  //     //console.log(JSON.stringify(data).);
  //   //setrequestId(JSON.stringify(data).requestId);
  //   setresultUrl(objData.datasetUrl);
  //   })
  //   .catch((error) => console.log(error));;
  //   };

  let content = (
     <div className="col-lg-12 text-center">
             <h1 className="mt-4">BK Anonymization</h1>
             {/* <AaaS fileName = {fileName}/> */}
            
    <div className="row" align="center" >
      <div className="col-lg-6" style={{ paddingLeft:'15rem'}} >
        <div  align="center">
      <div className="card border-primary mb-3 " align="center" style={{ maxWidth: '60rem',minHeight: '4rem' }}>
        <div className="card-header" style={{backgroundColor: '#1E90FF',color:'white'}}>Upload</div>
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

    
      <div className="card-deck-lg-6"  >
      <div className="card border-primary  mb-3"  style={{ 
        maxWidth: '60rem',minHeight: '27rem', maxHeight: '27rem',overflow:'auto'}}>
      {/* <div className="card border-primary  mb-3"  style={{ maxWidth: '60rem',minHeight: '10rem',
      maxHeight:'15rem',overflow:'auto'}}> */}
        <div className="card-header" style={{backgroundColor: '#1E90FF',color:'white'}}>Hierarchies files</div>
        <div className="card-body " align="center">
          <div className="row-lg-6" align="center">
          <table >
          {attributes.map(({ field }, index) =>
            (<AttributeCheckBox
              name={field}
              key={field}
              index={index}
              handleTypeSelect={handleTypeSelect}
            //   handleHierarchyUpload={handleHierarchyUpload}
            />))}
        </table>
          </div>
        </div>
      </div>

       </div>
      
      </div>


      <button className="btn btn-primary" onClick={(e) => handleRequest(e, 'anonymize')}>
            Anonymize
            </button>
            {renderWaitAnony}


      </div>
        <div className="col-lg-6"  align="center" style={{ paddingRight:'15rem'}}>
      <div className="card border-primary mb-3" style={{ maxWidth: '60rem',minHeight: '19.3rem', maxHeight: '35.2rem',overflow:'auto' }}>
        <div className="card-header" style={{backgroundColor: '#1E90FF',color:'white'}}>Privacy model</div>
        <div className="card-body">


          <PrivacyModelManager
          privacyModels={privacyModels}
          handlePrivacyAdd={handlePrivacyAdd}
          handlePrivacyRemove={handlePrivacyRemove}
          att={attributes}
        />

        <RenderPrivacyModels
          privacyModels={privacyModels}
          handlePrivacyRemove={handlePrivacyRemove}
        />
        </div>
      </div>

   
     
      {/* <button className="btn btn-primary" onClick={(e) => handlesendJson(e, 'sendJsonAnony')}>
            sendJsonAnony
              </button> */}
      {/* <button className="btn btn-primary" onClick={(e) => handlesendResult(e, 'sendResultAnony')}>
            sendResult
              </button> */}
      <button className="btn btn-primary" onClick={(e) => handlegetResult(e, 'getResultAnony')}>
            Result 
              </button>
              {renderWaitResult}
              {link}
              
              
              {/* <button className="btn btn-primary" onClick={(e) => handlegetLink()}>
            Result
              </button>
              {link} */}

              {/* <button className="btn btn-primary" onClick={(e) => handlegetLink()}>
            getLink
              </button>  */}

              {/* <button className="btn btn-primary" onClick={(e) => handlegetLinkAlter()}>
            getLink
              </button>  */}

              {/* <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal">getlin</button> */}
              {/* {dialogRes} */}
              
          </div>
               </div>
               </div>
  );

  return content;
};
export default Simple
