
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

Open [http://localhost:3000](http://localhost:3000) to view it in the browser.


## 'test'

File json định dạng mẫu trong thư mục "./arx-automation/my-app/src/dataExample"

Nút "sendJsonAnalyze" sẽ gửi thẳng local file "requestAnalyze.json" đến queue "analyze" ở rabbitMQ
Nút "sendJsonAnony" sẽ gửi thẳng local file "requestAnony.json" đến queue "anony" ở rabbitMQ
Nút "sendResultAnalyze" sẽ gửi thẳng local file "resultAnalyze.json" đến queue "result" ở rabbitMQ
Nút "sendResultAnony" sẽ gửi thẳng local file "resultAnony.json" đến queue "result" ở rabbitMQ


Nút "ResultAnalyze" sẽ lấy file json ở queue "result" ở rabbitMQ về hiển thị kết quả Analyze
Nút "ResultAnony" sẽ lấy file json ở queue "result" ở rabbitMQ về hiển thị kết quả Anony

## 'Lưu ý'
-rabbitMQ gồm 3 queue: "analyze" ,"anony"  ,"result"
-Định dạng json cần giống mẫu trong thư mục "./arx-automation/my-app/src/dataExample" để chạy.
-"ResultAnalyze"  và "ResultAnony" cùng nằm trong queue "result" nhưng định dạnh khác nhau
-Phiên bản này để test nên không cần điền các thông số trong frontend, load thẳng file json từ local.


