const express = require('express');
const path = require('path');
const amqp = require('amqplib/callback_api');
const app = express();
var bodyParser = require('body-parser');
var bodyParser = require('body-parser');
app.use(bodyParser.json({limit: '50mb'}));
app.use(bodyParser.urlencoded({limit: '50mb', extended: true}));
//app.use(bodyParser.json());
//app.use(bodyParser.urlencoded({ extended: false }));
var cors = require("cors");
app.use(cors());
//const fs = require('fs');
//let student = { 
    
  //  gender: 'Male',
  //  department: 'English',
  //  car: 'Honda' 
//};
//let data = JSON.stringify(student);
//fs.writeFileSync('student-2.json', data);

// Serve the static files from the React app
//app.use(express.static(path.join(__dirname, 'client/build')));

// An api endpoint that returns a short list of items
app.get('/api/test', (req,res) => {
	var list = ["item1", "item2", "item3","item4"];
	res.json(list);
	console.log('Sent list of items');
});

var tempData={};
//let books = [];
const url='amqp://localhost';
//let chanelAnomyzation = null;
//let chanelAnomyzation = null;
let chanelAnony = null;
let chanelAnalyze=null;
let chanelResult=null;
var queueAnony='anony';
var queueAnalyze='analyze';
var queueResult='result';

amqp.connect(url, function (err, conn) {
	conn.createChannel(function (err, channel) {
		channel.assertQueue(queueAnony, {
            durable: false
        });
		chanelAnony = channel;
	   //consumeFromQueue(queueName);
	});
	conn.createChannel(function (err, channel) {
		channel.assertQueue(queueAnalyze, {
            durable: false
        });
		chanelAnalyze = channel;
	   //consumeFromQueue(queueName);
	});
	conn.createChannel(function (err, channel) {
		channel.assertQueue(queueResult, {
            durable: false
        });
		chanelResult = channel;
		consumeFromQueue(chanelResult,queueResult);
	   //consumeFromQueue(queueName);
	});
 });

function publishToQueue (chanelName,queueName, data) {
	console.log(" send 1 message");
	return chanelName.sendToQueue(queueName, Buffer.from(JSON.stringify(data)));
 }

  
 function consumeFromQueue (chanelName,queueName) {
	chanelName.consume(queueName, function(msg) {
		console.log(" received 1 message");
		tempData = JSON.stringify(JSON.parse(msg.content.toString()));
		//console.log(dataTest);
		//fs.writeFileSync('student-2.json', dataTest);
		//console.log(tempData);
   		return tempData;
		
		//return msg.content;
	}, {
		noAck: true
	});
 }
 
app.post('/api/anonymize', function (req, res) {
	var data = req.body;
	//console.log(data);
	publishToQueue(chanelAnomyzation,queueAnomyzation, data);
  	//res.statusCode = 200;
  	//res.data = {"message-sent":true};
	//var newBook = req.body;
	//books.push(newBook)
	res.json(data);
	//res.send("hi");
})

app.post('/api/analyze', function (req, res) {
	var data = req.body;
	publishToQueue(chanelAnalyze,queueAnalyze, data);
	res.json(data);
})

app.post('/api/sendJsonAnalyze', function (req, res) {
	var data = req.body;
	publishToQueue(chanelAnalyze,queueAnalyze, data);
	res.json(data);
})

app.post('/api/sendJsonAnony', function (req, res) {
	var data = req.body;
	publishToQueue(chanelAnony,queueAnony, data);
	res.json(data);
})

app.post('/api/sendResultAnalyze', function (req, res) {
	var data = req.body;
	publishToQueue(chanelResult,queueResult, data);
	res.json(data);
})

app.post('/api/sendResultAnony', function (req, res) {
	var data = req.body;
	publishToQueue(chanelResult,queueResult, data);
	res.json(data);
})

app.post('/api/getResultAnalyze', function (req, res) {
	//var list = ["item1", "item2", "item3","item4"];
	var data=tempData;
	//var data = consumeFromQueue (chanelResult,queueResult);
	//console.log(typeof data);
	//res.header('Access-Control-Allow-Origin', '*');
	//res.send(JSON.stringify(data));
	res.json(JSON.parse(data));
})

app.post('/api/getResultAnony', function (req, res) {
	var data=tempData;
	res.json(JSON.parse(data));
})









// Handles any requests that don't match the ones above
//app.get('*', (req,res) =>{
//	res.sendFile(path.join(__dirname+'/client/build/index.html'));
//});

const port = process.env.PORT || 5000;
app.listen(port);

console.log('App is listening on port ' + port);
