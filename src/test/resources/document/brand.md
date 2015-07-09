# Group 品牌管理 

品牌屬性:

 Field | Required | Type | default | Format | Description 
-------|----------|------|---------|--------|-------------
confirm|Y|string|||审核码
createDate|N|date|||创建日期
createUserUuid|N|string|||创建用户_uuid
modiUserUuid|N|string|||修改用户_uuid(modify_uuid)
name|Y|string|||品牌名称
no|Y|string|||品牌编号
path|N|string|||品牌照片路径
status|Y|string|||状态码
terminalIp|N|string|||终端IP
terminalMac|N|string|||终端MAC
terminalType|N|string|||终端类型
type|N|string|||品牌类型(集团品牌,经销商自主品牌)
uuid|N|string|||uuid

## 品牌清單 [/brands]

### 取得品牌清單 [GET]

``` bash
curl 'http://localhost:8080/brands' -H "Content-Type: application/json"
```

+ Response 200 (application/json;charset=UTF-8)

			[ {
			  "uuid" : "001",
			  "no" : "no1",
			  "name" : "name1",
			  "type" : null,
			  "path" : null,
			  "status" : "Y",
			  "confirm" : "Y",
			  "terminalType" : null,
			  "terminalMac" : null,
			  "terminalIp" : null,
			  "createUserUuid" : null,
			  "createDate" : null,
			  "modiUserUuid" : null
			}, {
			  "uuid" : "002",
			  "no" : "no2",
			  "name" : "name2",
			  "type" : null,
			  "path" : null,
			  "status" : "Y",
			  "confirm" : "Y",
			  "terminalType" : null,
			  "terminalMac" : null,
			  "terminalIp" : null,
			  "createUserUuid" : null,
			  "createDate" : null,
			  "modiUserUuid" : null
			} ]

### 新增品牌 [POST]

Arguments:

 Field | Required | Type | default | Format | Description 
-------|----------|------|---------|--------|-------------
name|Y|string|||品牌名称
no|Y|string|||品牌编号

``` bash
curl 'http://localhost:8080/brands' -X POST -H "Content-Type: application/json" -d '{
  "no" : "001",
  "name" : "我爱我家"
}'
```

+ Request (application/json)

			{
			  "no" : "001",
			  "name" : "我爱我家"
			}

+ Response 201 (application/json;charset=UTF-8)

			{
			  "uuid" : null,
			  "no" : "001",
			  "name" : "我爱我家",
			  "type" : null,
			  "path" : null,
			  "status" : "Y",
			  "confirm" : "N",
			  "terminalType" : null,
			  "terminalMac" : null,
			  "terminalIp" : null,
			  "createUserUuid" : null,
			  "createDate" : null,
			  "modiUserUuid" : null
			}

## 品牌 [/brands/{id}]

### 取得品牌 [GET]

``` bash
curl 'http://localhost:8080/brands/001' -H "Content-Type: application/json"
```

+ Response 200 (application/json;charset=UTF-8)

			{
			  "uuid" : "001",
			  "no" : "no1",
			  "name" : "name1",
			  "type" : null,
			  "path" : null,
			  "status" : "Y",
			  "confirm" : "Y",
			  "terminalType" : null,
			  "terminalMac" : null,
			  "terminalIp" : null,
			  "createUserUuid" : null,
			  "createDate" : null,
			  "modiUserUuid" : null
			}

### 更新品牌 [PATCH]

``` bash
curl 'http://localhost:8080/brands/001' -X PATCH -H "Content-Type: application/json" -d '{
  "name" : "new_name"
}'
```

+ Request (application/json)

			{
			  "name" : "new_name"
			}

+ Response 200 (application/json;charset=UTF-8)

			{
			  "uuid" : "001",
			  "no" : "no1",
			  "name" : "new_name",
			  "type" : null,
			  "path" : null,
			  "status" : "Y",
			  "confirm" : "Y",
			  "terminalType" : null,
			  "terminalMac" : null,
			  "terminalIp" : null,
			  "createUserUuid" : null,
			  "createDate" : null,
			  "modiUserUuid" : null
			}

### 刪除品牌 [DELETE]

``` bash
curl 'http://localhost:8080/brands/001' -X DELETE -H "Content-Type: application/json"
```

+ Response 204