# arx-automation

## Yêu cầu

... WIP ...

## Chạy CKAN local

Cần có `docker` và `docker-compose`.

_Chỉ copy file `.env.template` nếu chưa có file `.env`._

```sh
cd ckan/ckan/contrib/docker
cp .env.template .env
docker-compose up
```

Chờ đến khi chạy xong hết thấy dòng: `Running server 0.0.0.0 on port 5000" thì truy cập CKAN tại `http://localhost:5000`
