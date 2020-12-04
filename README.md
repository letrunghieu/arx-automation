# arx-automation

## Yêu cầu

Sau khi clone về:

```sh
git submodules init
git submodules update --remote
```

Các dữ liệu mẫu nằm trong thư mục `data`.
Các cây phân cấp tương ứng của các thuộc tính trong dữ liệu mẫu nằm trong thư mục `hierarchies`.

## Chạy CKAN local

Cần có `docker` và `docker-compose`.

_Chỉ copy file `.env.template` nếu chưa có file `.env`._

```sh
cp .env.template .env
docker-compose up
```

Chờ đến khi chạy xong hết thấy dòng: `Running server 0.0.0.0 on port 5000" thì truy cập CKAN tại `http://localhost:5000`

Tạo một yêu cầu ẩn danh hoá bằng command line (tập adults, 2 QI là age và education)

```
./gradlew frontEnd --args="--new -d adult -q age,education"
```

Chạy công cụ ẩn danh hoá cho yêu cầu vừa tạo

```
./gradlew run
```

Các tập dữ liệu được lưu trong database `anonymization` của MongoDB. Truy cập giao diện quản lý của MongoDB tại `http://localhost:8081`, các tập dữ liệu được định danh thông qua `_id`.


## Demo

```shell script

# #1 console
docker-compose up

docker-compose exec -u root ckan bash
ckan -c /etc/ckan/ckan.ini sysadmin add ckan email=letrunghieu.cse09@gmail.com name=ckan
chown ckan:ckan -r /var/lib/ckan/storage/uploads/

# #2 console
npm i
npm start

# #3 console
cd my-app
npm i
npm start

# #4 console
./gradlew bootRun
```
