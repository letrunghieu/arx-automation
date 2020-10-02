import React, { useState } from 'react'

const PrivacyModelManager = (props) => {
//console.log(myAtt);
var kanonymity = (
    <div>
        <label>K: </label>
        {/* <input type='number' className="form-control" min="2" max="1000" onChange={(e) => handleSetParam({k: e.target.value})}></input> */}
        <input type='number'  min="2" max="1000" defaultValue="5"  id="K" onChange={(e) => handleSetParam({k: e.target.value})}></input>
        <br/>
    </div>
);

    var myAtt= props.att;
    var first="";
    if(myAtt[0])
    {
        first=myAtt[0].field;
    }
    var ldiversity = (

        <div>
             <label>L: </label>
            <input type='number' min="2" max="1000" id="L" defaultValue="5" onChange={(e) => handleSetParam({l: e.target.value , column_name: document.getElementById("column_name").value })}></input>
             <br/>
    <label htmlFor="column_name">Choose a Sensitive attribute:</label>
    
    {/* <select name="column_name" id="column_name" defaultValue={first} onChange={(e) => handleSetParam({l: document.getElementById("L").value, column_name: e.target.value})}> */}

    <select name="column_name" id="column_name" defaultValue={first}  onChange={(e) => handleSetParam({l: document.getElementById("L").value, column_name: e.target.value})}>
       { myAtt.map( ({field},index ) => (<option value={field} key={index}>{field}</option>))}
    </select>
        </div>
    );
    const {handlePrivacyAdd } = props;
    const [selectedPrivacyModel, setSelectedPrivacyModel] = useState('KANONYMITY');
    const [param, setParam] = useState({k: "5"});
    //const [lastParamK,setlastParamK]= useState({k: "5"});
    //const [lastParamL,setlastParamL]= useState({l: "5",column_name:first});

    const [paramForm, setParamForm] = useState(kanonymity);

    const handleSetParam = (newParam) => {
        setParam(newParam);
    };  
    


    // const ldiversity = (
    //     <div>
    //         <label>L: </label>
    //         <input type='number' min="2" max="1000" id="L" onChange={(e) => handleSetParam({l: e.target.value , column_name: document.getElementById("column_name").value })}></input>
    //         <br/>
    //     <label>Field: </label>
        
    //         <input type='text' id="column_name" onChange={(e) => handleSetParam({l: document.getElementById("L").value, column_name: e.target.value})}></input>
    //     </div>
    // );

    
    const tcloseness = (
        <div>
            <label>T: </label>
            <input type='number' min="0.000001" max="1" step="0.000001" id="T" onChange={(e) => handleSetParam({t: e.target.value , column_name: document.getElementById("column_name").value })}></input>
            <br/>
            <label>Field: </label>
            <input type='text' id="column_name" onChange={(e) => handleSetParam({t: document.getElementById("T").value, column_name: e.target.value})}></input>
        </div>
    );
    const ldiversityRecursive = (
        <div>
            <label>L: </label>
            <input type='number'  min="2" max="1000" id="L" onChange={(e) => handleSetParam({l: e.target.value , column_name: document.getElementById("column_name").value , C: document.getElementById("C").value})}></input>
            <br/>
            <label>Field: </label>
            <input type='text' id="column_name" onChange={(e) => handleSetParam({l: document.getElementById("L").value, column_name: e.target.value , C: document.getElementById("C").value})}></input>
            <br/>
            <label>C: </label>
            <input type='number'  min="0.00001" max="1000" step="0.00001" id="C" onChange={(e) => handleSetParam({l: document.getElementById("L").value , column_name: document.getElementById("column_name").value, c: e.target.value })}></input>
        </div>
    );



    const updateForm = (form) => {
        
        if (form === 'KANONYMITY') {
            
            setParamForm(kanonymity);
            //setlastParamK(lastParamK);
            // if(document.getElementById("K"))
            // {
            // handleSetParam({k: document.getElementById("K").value})
            // }
        } else if(form === 'LDIVERSITY_DISTINCT' || form === 'LDIVERSITY_GRASSBERGERENTROPY' || form === 'LDIVERSITY_SHANNONENTROPY') {
            setParamForm(ldiversity);
            // console.log(document.getElementById("L"));
            // if( document.getElementById("L") && document.getElementById("column_name"))
            // {
            //     console.log("Sssssssss");
            // handleSetParam({l: document.getElementById("L").value, column_name: document.getElementById("column_name").value})
            // }
        } else if(form === 'TCLOSENESS_ORDERED_DISTANCE' || form === 'TCLOSENESS_EQUAL_DISTANCE') {
            setParamForm(tcloseness)
        } else if (form === 'LDIVERSITY_RECURSIVE') {
            setParamForm(ldiversityRecursive)
        } else {
            setParamForm(<div>No known form selected</div>)
        }
    };

    let content = (
        <div>

            <select
                className="form-control"
                onChange={(e) => {
                    setSelectedPrivacyModel(e.target.value);
                    updateForm(e.target.value)
                }
                }
            >
                <option defaultValue value="KANONYMITY">K-Anonymity</option>
                <option value="LDIVERSITY_DISTINCT">L-Diversity-Distinct</option>
                {/* <option value="LDIVERSITY_GRASSBERGERENTROPY">L-Diversity-Grassberger-Entropy</option>
                <option value="LDIVERSITY_SHANNONENTROPY">L-Diversity-Shannon-Entropy</option> */}
                {/* <option value="LDIVERSITY_RECURSIVE">L-Diversity-Recursive</option> */}
                {/* <option value="TCLOSENESS_ORDERED_DISTANCE">T-Closeness Ordered Distance</option> */}
                {/* <option value="TCLOSENESS_EQUAL_DISTANCE">T-Closeness Equal Distance</option> */}
            </select>
            <br/>
            {paramForm}
            <br/>
            <button className="btn btn-outline-info" onClick={() => handlePrivacyAdd({privacyModel: selectedPrivacyModel, params: param})}>Add Privacy Model</button>
        </div>
    );
    return content
};

export default PrivacyModelManager
