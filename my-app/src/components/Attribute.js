import React, { useState } from 'react'

const Attribute = props => {

    const [currentSelected, setSelect] = useState("INSENSITIVE");
    const { name, index } = props;

    let content = (
        <tbody >
            <tr >
                <td>{name}</td>
                <td>
                    <select 
                        className="form-control"
                        onChange={(e) => {
                            props.handleTypeSelect(e, name, index);
                            setSelect(e.target.value)
                        }
                        }
                    >
                       {/*  <option defaultValue value={currentSelected}>Quasi-identifying</option> */}
                        {/* <option value="INSENSITIVE">Insensitive</option> */}
                        <option defaultValue value="INSENSITIVE">Insensitive</option>
                        <option value='QUASIIDENTIFYING'>Quasi-identifying</option>
                        <option value="SENSITIVE">Sensitive</option>
                        <option value="IDENTIFYING">Identifying</option>
                    </select>
                </td>
                <td>
                    <input type='file' onChange={(e) => props.handleHierarchyUpload(e.target.files[0], name, index)}></input>
                </td>
            </tr>
        </tbody>
    );
    return content
};

export default Attribute
