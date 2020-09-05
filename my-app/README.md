
## Yêu cầu
- Đã cài đặt : npm
- server rabbitmq đang hoạt động ( có thể xem ở [http://localhost:15672](http://localhost:15672) với tài khoản (guest:guest))

## Cai dat
Ở thư mục gốc "./arx-automation", chạy lệnh sau để cài đặt express module:<br />
sudo npm install<br />

Ở thư mục "./arx-automation/my-app", chạy lệnh sau để cài đặt react module:<br />
sudo npm install<br />

## 'npm start'

Ở thư mục gốc "./arx-automation", chạy lệnh sau để run express:
npm start.<br />

Ở thư mục gốc "./arx-automation/my-app/src", chạy lệnh sau để run react frontend:
npm start.<br />

Open [http://localhost:3000](http://localhost:3000) to view it in the browser.<br />


## 'test'<br />

File json định dạng mẫu trong thư mục "./arx-automation/my-app/src/dataExample" <br />

Nút "sendJsonAnalyze" sẽ gửi thẳng local file "requestAnalyze.json" đến queue "analyze" ở rabbitMQ <br />
Nút "sendJsonAnony" sẽ gửi thẳng local file "requestAnony.json" đến queue "anony" ở rabbitMQ <br />
Nút "sendResultAnalyze" sẽ gửi thẳng local file "resultAnalyze.json" đến queue "result" ở rabbitMQ <br />
Nút "sendResultAnony" sẽ gửi thẳng local file "resultAnony.json" đến queue "result" ở rabbitMQ <br />
Nút "ResultAnalyze" sẽ lấy file json ở queue "result" ở rabbitMQ về hiển thị kết quả Analyze<br />
Nút "ResultAnony" sẽ lấy file json ở queue "result" ở rabbitMQ về hiển thị kết quả Anony<br />

## 'Lưu ý'<br />
-rabbitMQ gồm 3 queue: "analyze" ,"anony"  ,"result"<br />
-Định dạng json cần giống mẫu trong thư mục "./arx-automation/my-app/src/dataExample" để chạy.<br />
-"ResultAnalyze"  và "ResultAnony" cùng nằm trong queue "result" nhưng định dạnh khác nhau<br />
-Phiên bản này để test nên không cần điền các thông số trong frontend, load thẳng file json từ local.<br />


