const express = require('express');
const path = require('path');
const mongoose = require('mongoose');
const amqp = require('amqplib/callback_api');
const app = express();
var bodyParser = require('body-parser');
var cors = require("cors");
app.use(bodyParser.json({limit: '50mb'}));
app.use(bodyParser.urlencoded({limit: '50mb', extended: true}));
app.use(cors());


var optionMongo = {
    auth: {
        "authSource": "admin"
    },
    user: "root",
    pass: "secret", useNewUrlParser: true, useUnifiedTopology: true
};


var urlMongo = "mongodb://localhost:27017/anonymization?ssl=false";
var myDB = mongoose.connect(urlMongo, optionMongo, function (err) {
        if (err) throw err;
        console.log('Successfully connected');
    }
);


var hierarchiesConstruction = new mongoose.Schema(
    {
        data: Array
    }
)

var originalDatasetsConstruction = new mongoose.Schema(
    {
        data: Array
    }
)
var solutionsConstruction = new mongoose.Schema(
    {}
)

var outputDatasetsConstruction = new mongoose.Schema(
    {}
)


var requestsConstruction = new mongoose.Schema(
    {
        title: String,
        originalFileName: String,
        datasetId: String,
        hierarchies: Array,
        sensitiveAttributes: Array,
        identifierAttributes: Array,
        anonymizeModelConfigs: {
            suppressionLimit: Number,
            k: Number,
            l: Number
        }
    }
)

var hierarchies = mongoose.model('hierarchies', hierarchiesConstruction, 'hierarchies');
var originalDatasets = mongoose.model("originalDatasets", originalDatasetsConstruction, "originalDatasets");
////////////////////////////


// Ko viet hoa dc "originalDatasets"

//////////////////////////////////////
var requests = mongoose.model('requests', requestsConstruction, 'requests');

function convertDatatoHieu(dataBody) {
    var jsonModel = {};
    var sensitive = [];
    var identifier = [];
    var anonymizeModel = {};
    var hierarchy = [];
    //console.log(data);
    for (var i = 0; i < dataBody.privacyModels.length; i++) {
        if (dataBody.privacyModels[i].privacyModel === "KANONYMITY") {
            anonymizeModel['k'] = parseInt(dataBody.privacyModels[i].params.k);
        } else if (dataBody.privacyModels[i].privacyModel === "LDIVERSITY_DISTINCT") {
            anonymizeModel['l'] = parseInt(dataBody.privacyModels[i].params.l);
            sensitive.unshift(dataBody.privacyModels[i].params.column_name);
            for (var j = 0; j < dataBody.attributes.length; j++) {
                if (dataBody.attributes[j].field === dataBody.privacyModels[i].params.column_name) {
                    dataBody.attributes[j].attributeTypeModel = "SENSITIVE";
                }
            }
        }
    }


    for (var i = 0; i < dataBody.attributes.length; i++) {
        ///////////////hierarchies
        if (dataBody.attributes[i].attributeTypeModel === "QUASIIDENTIFYING") {
            if (dataBody.attributes[i].hierarchy) //neu co cay phan cap
            {
                var dataHierarchies = new hierarchies({data: dataBody.attributes[i].hierarchy});

                dataHierarchies.save();
                var hierachyForrequest = {
                    attribute: dataBody.attributes[i].field, //ten filed cua hierachy hien tai
                    datasetId: dataHierarchies._id
                }
                hierarchy.push(hierachyForrequest);
            } else {
                var hierachyForrequest = {
                    attribute: dataBody.attributes[i].field, //ten filed cua hierachy hien tai
                    datasetId: null
                }
                hierarchy.push(hierachyForrequest);
            }
        }

        //////////////sensitive
        if (dataBody.attributes[i].attributeTypeModel === "SENSITIVE") {
            if (sensitive.indexOf(dataBody.attributes[i].field) === -1) {
                sensitive.push(dataBody.attributes[i].field);
            }
        }
        ///////////////////// identifier
        if (dataBody.attributes[i].attributeTypeModel === "IDENTIFYING") {
            identifier.push(dataBody.attributes[i].field);
        }
    }


    anonymizeModel['suppressionLimit'] = dataBody.suppressionLimit;

    var datasetId = null;
    var ckanUrl = null;
    if (dataBody.data) {
        var dataOrigin = new originalDatasets({data: dataBody.data});//data.data=lay field data trong databody
        dataOrigin.save();
        datasetId = dataOrigin._id;
    } else if (dataBody.ckanUrl) {
        ckanUrl = dataBody.ckanUrl
    }


    var requestData = new requests({
        title: dataBody.title,
        originalFileName: dataBody.originalfileName,
        datasetId: datasetId,
        ckanUrl: ckanUrl,
        hierarchies: hierarchy,
        sensitiveAttributes: sensitive,
        identifierAttributes: identifier,
        anonymizeModelConfigs: anonymizeModel
    });
    requestData.save();


    jsonModel['id'] = requestData._id;
    jsonModel['title'] = dataBody.title;
    jsonModel['datasetId'] = datasetId;
    jsonModel['ckanUrl'] = ckanUrl;
    jsonModel['hierarchies'] = hierarchy;
    jsonModel['sensitiveAttributes'] = sensitive;
    jsonModel['identifierAttributes'] = identifier;
    jsonModel['anonymizeModelConfigs'] = anonymizeModel;
    jsonModel["originalFileName"] = dataBody.originalfileName;
    console.log(jsonModel);
    return jsonModel;
}


var tempData = {};
const urlMQ = 'amqp://localhost';
let chanelRequest = null;
let chanelResult = null;
var queueRequest = 'requests';
var queueResult = 'results';



const WebSocketServer = require('ws').Server;
const wsServer = new WebSocketServer({
    host: 'localhost',
    port: 5011,
    clientTracking: true,
}, () => {console.log('WS server is ready');});


wsServer.on('connection', function (ws, request) {
    console.log("A new client is connected: " + wsServer.clients.size);
    ws.send('Welcome!');
});



amqp.connect(urlMQ, function (err, conn) {

    conn.createChannel(function (err, channel) {
        // channel.assertQueue(queueRequest, {
        //       durable: false
        //    });
        chanelRequest = channel;
    });

    conn.createChannel(function (err, channel) {
        // channel.assertQueue(queueResult, {
        //        durable: false
        //    });
        chanelResult = channel;
        // consumeFromQueue(chanelResult, queueResult);
        chanelResult.consume('results', function (msg) {
            console.log(" received 1 message");
            tempData = JSON.stringify(JSON.parse(msg.content.toString()));
            console.log(tempData);
            wsServer.clients.forEach(ws => {
                ws.send(tempData);
            })
            return tempData;
        }, {
            noAck: true
        });
    });
});
//////////////////////////////////////bin

// Su dung exchange.pulish()  bi loi

//////////////////////////////////////////////
function publishToQueue(chanelName, queueName, data) {
    console.log(" send 1 message");
    return chanelName.publish('anonymization', queueName, Buffer.from(JSON.stringify(data)));
}


function consumeFromQueue(chanelName, queueName) {
    chanelName.consume(queueName, function (msg) {
        console.log(" received 1 message");
        tempData = JSON.stringify(JSON.parse(msg.content.toString()));
        console.log(tempData)
        return tempData;
    }, {
        noAck: true
    });
}

app.post('/api/sendResultAnony', function (req, res) {
    var data = req.body;
    publishToQueue(chanelResult, queueResult, data);
    res.json(data);
})


app.post('/api/getResultAnony', function (req, res) {
    var data = tempData;
    res.json(JSON.parse(data));
})


app.post('/api/anonymize', function (req, res) {
    var dataBody = req.body;
    var data = convertDatatoHieu(dataBody);
    publishToQueue(chanelRequest, queueRequest, data);
    res.json(data);
})

// app.post('/api/sendJsonAnony', function (req, res) {
//     var dataBody = req.body;
//     var transData = convertDatatoHieu(dataBody);
//     publishToQueue(chanelRequest, queueRequest,transData);
//     res.json(transData);
// })


const port = process.env.PORT || 5010;
// app.listen(port);


const server = require('http').createServer(app);
server.listen(port);

wsServer.on('error', console.log);
console.log('App is listening on port ' + port);
