import React from 'react'
import ReactTable from "react-table";
import 'react-table/react-table.css'
//import "./test.css"
const DistributionOfRisk = props => {

    const { riskIntervalList } = props;
    
    const columns = [{
        //Header:()=><span color="red">'Interval'</span>,
        Header: 'Interval',
        accessor: 'interval'
    },
    {
    
        Header: 'RecordsWithRisk',
        
        accessor: 'recordsWithRiskWithinInterval',
        Cell: valueHere=> <span className='number'>{valueHere.value.toFixed(5)}</span>
    },
    {
        Header: 'RecordsWithMaxmalRisk',
        accessor: 'recordsWithMaximalRiskWithinInterval',
        Cell: valueHere=> <span className='number'>{valueHere.value.toFixed(5)}</span>
    },]
    //console.log(riskIntervalList);
    //console.log(typeof riskIntervalList[0].recordsWithRiskWithinInterval);
    let content = (
        <div>
            <h3>Risk Interval</h3>
            <ReactTable  
            
                data={riskIntervalList}
                columns={columns}
                defaultPageSize={24}
            />
        </div>
    )
    return content
}
export default DistributionOfRisk