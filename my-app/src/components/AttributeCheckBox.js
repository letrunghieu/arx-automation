import React, { useState } from 'react'

const AttributeCheckBox = props => {

    const [currentSelected, setSelect] = useState('QUASIIDENTIFYING');
    const [isChecked, setisChecked] = useState(true);

    const { name, index } = props;

    // const handleChange = (e) =>
    // {
    //     setisChecked(!isChecked);
    // }
    let content = (
        <tbody >
            <tr >
                <td>{name}</td>
                <td>
                <input type="checkbox" id={name} defaultChecked='true'  value='QUASIIDENTIFYING' key={index} onClick={(e) => {
                            //setisChecked(!isChecked);
                          
                            //console.log(e.target.checked);
                            if(e.target.checked) {
                                setSelect('QUASIIDENTIFYING');
                                e.target.value='QUASIIDENTIFYING';
                            }
                            else {
                                setSelect('INSENSITIVE');
                                e.target.value='INSENSITIVE';
                            }
                            props.handleTypeSelect(e, name, index);
                           
                        }
                        }  />
                </td>
                <td>
                    {/* <input type='file' onChange={(e) => props.handleHierarchyUpload(e.target.files[0], name, index)}></input> */}
                </td>
            </tr>
        </tbody>
    );
    return content
};

export default AttributeCheckBox
