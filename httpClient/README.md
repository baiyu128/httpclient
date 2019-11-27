# IntelliJ IDEA 插件 HTTP Client



## 界面客户端

* 使用手册

https://www.jetbrains.com/help/idea/testing-restful-web-services.html


* Tools -> HTTP Client -> Test RESTful Web Service

![](https://ws3.sinaimg.cn/large/006tKfTcgy1g0v80r2s3ej30e60a3dg8.jpg)


![](https://ws1.sinaimg.cn/large/006tKfTcgy1g0vi5l1uyuj31c00u00vg.jpg)



## 文本客户端

* 使用手册

https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html


### 特点

- 纯文本编写
- 支持统一配置
- 支持 scripts 脚本


### 创建新的请求文件

* Scratch files （全局文件）
* physical files（项目文件）


![](https://ws3.sinaimg.cn/large/006tKfTcgy1g0vitffcihj31c00u00uw.jpg)

### live templates

* 'gtrp' and 'gtr' create a GET request with or without query parameters;
* 'ptr' and 'ptrp' create a POST request with a simple or parameter-like body;
* 'mptr' and 'fptr' create a POST request to submit a form with a text or file field (multipart/form-data);


* Live Templates 演示

![](https://ws1.sinaimg.cn/large/006tKfTcgy1g0xnyirr3qg30w20i547l.gif)


* Live Templates 配置

![](https://ws2.sinaimg.cn/large/006tKfTcgy1g0vigan9atj31780u0djt.jpg)



### 支持 HTTP 1.1 所有方法

POST、GET、PUT、DELETE、HEAD、OPTIONS、TRACE、CONNECT


* GET

```js
### Get request with a header
GET https://httpbin.org/ip
Accept: application/json

### Get request with parameter
GET https://httpbin.org/get?show_env=1
Accept: application/json

### Get request with environment variables
GET {{host}}/get?show_env={{show_env}}
Accept: application/json

### Get request with disabled redirects
# @no-redirect
GET http://httpbin.org/status/301
```


* POST

```js
### Send POST request with json body
POST https://httpbin.org/post
Content-Type: application/json

{
  "id": 999,
  "value": "content"
}

### Send POST request with body as parameters
POST https://httpbin.org/post
Content-Type: application/x-www-form-urlencoded

id=999&value=content

### Send a form with the text and file fields
POST https://httpbin.org/post
Content-Type: multipart/form-data; boundary=WebAppBoundary

--WebAppBoundary
Content-Disposition: form-data; name="element-name"
Content-Type: text/plain

Name
--WebAppBoundary
Content-Disposition: form-data; name="data"; filename="data.json"
Content-Type: application/json

< ./request-form-data.json
--WebAppBoundary--

###
```



### 演示项目简介

* Spring Boot - Spring Security
* 首页 http://localhost:8080/index.html
* 登陆页面 http://localhost:8080/login.html
* 测试接口(有登陆验证) http://localhost:8080/api/security/test
* 测试账号，用户名： lee，密码：123456


* 用浏览器直接访问测试接口会被强制跳转到登陆页面，登陆成功后返回接口调用结果。
* 原理是登陆校验成功返回 JSESSIONID，客户端将 JSESSIONID 存到 Cookie ，服务端根据 Cookie 验证是否登陆。


![](https://ws4.sinaimg.cn/large/006tKfTcgy1g0vjnuizmog30pj0hqaam.gif)



### 演示项目接口调用实战


#### 登陆接口

```js
POST http://localhost:8080/api/login
Content-Type: application/x-www-form-urlencoded

account=lee&password=123456

// 返回
HTTP/1.1 302 
Set-Cookie: JSESSIONID=A323D83D5E062588F4ACA433E891EA67; Path=/; HttpOnly
Location: http://localhost:8080/index.html
... 略
```


#### 测试接口

```js
GET http://localhost:8080/api/security/test
Accept: application/json
Cookie: JSESSIONID=A323D83D5E062588F4ACA433E891EA67

// 返回
HTTP/1.1 200 

you have been authenticated

```


#### 查看请求历史


* 点击右上角的按钮 ![Show HTTP Requests History](https://www.jetbrains.com/help/img/idea/2018.3/icons.vcs.history.svg@2x.png)

![](https://ws3.sinaimg.cn/large/006tKfTcgy1g0xnd91e2ag30w20i5qjd.gif)


* Tools | HTTP Client | Show HTTP Requests History

![](https://ws4.sinaimg.cn/large/006tKfTcgy1g0xngeddspj30si0n4wg6.jpg)




### 演示接口重构 - 统一配置


#### 通用配置，域名/端口

* `rest-client.env.json` 或 `http-client.env.json`

```json
{
  "default": {

  },
  "local": {
    "host": "http://localhost:8080"
  }
}
```


#### 个人安全配置，用户名/密码

* `rest-client.private.env.json` 或 `http-client.private.env.json`

```json
{
  "default": {

  },
  "local": {
    "account": "lee",
    "password": "123456"
  }
}
```


#### 重构后的请求文件

```js
### 登陆
POST {{host}}/api/login
Content-Type: application/x-www-form-urlencoded

account=lee&password=123456

### 测试接口
GET {{host}}/api/security/test
Accept: application/json
Cookie: JSESSIONID=1C1DD3EB60DEE60664FB0BFE0F1C9942

###
```


#### 运行请求

点击运行按钮，可以选择对应的环境

![](https://ws1.sinaimg.cn/large/006tKfTcgy1g0xlokoo5hg30w30i27ap.gif)



### 使用 response handler scripts


#### 引用方式


* 直接引用

```js
GET host/api/test

> {%
// Response Handler Script
...
%}
```


* 文件引用

```js
GET host/api/test

> scripts/my-script.js
```



#### 主要方法

[HTTP Response handling API reference](https://www.jetbrains.com/help/idea/http-response-handling-api-reference.html)


* client
    * client.global
      * set(varName, varValue) // 设置全局变量
      * get(varName) // 获取全局变量
      * isEmpty // 检查 global 是否为空
      * clear(varName) // 删除变量
      * clearAll // 删除所有变量
    * client.test(testName, func) // 创建一个名称为 `testName` 的测试
    * client.assert(condition, message) // 校验条件 `condition` 是否成立，否则抛出异常 `message`
    * client.log(text) // 打印日志


* response
    * response.body // 字符串 或  JSON (如果`content-type` 为 `application/json`.)
    * response.headers
        * valueOf(headerName) // 返回第一个匹配 headerName 的值，如果没有匹配的返回 null
        * valuesOf(headerName) // 返回所有匹配 headerName 的值的数组，如果没有匹配的返回空数组
    * response.status // Http 状态码，如： 200 / 400
    * response.contentType
        * mimeType // 返回 MIME 类型，如：`text/plain`, `text/xml`, `application/json`.
        * charset // 返回编码 UTF-8 等


* 方法调用示例

```js
GET https://httpbin.org/status/200

> {%
    client.test("Request executed successfully", function() {
        client.assert(response.status === 200, "Response status is not 200");
    });
%}
```



### 演示接口重构 - 动态更新 Cookie

```js
### 登陆
POST {{host}}/api/login
Content-Type: application/x-www-form-urlencoded

account={{account}}&password={{password}}

> {% client.global.set("Set-Cookie", response.headers.valueOf("Set-Cookie")) %}

### 测试接口
GET {{host}}/api/security/test
Accept: application/json
Cookie: {{Set-Cookie}}
```



### 演示接口重构 - 测试接口返回

```js
### 登陆
POST {{host}}/api/login
Content-Type: application/x-www-form-urlencoded

account={{account}}&password={{password}}

> {% client.global.set("Set-Cookie", response.headers.valueOf("Set-Cookie")) %}

### 测试接口
GET {{host}}/api/security/test
Accept: text/plain
Cookie: {{Set-Cookie}}

> login_demo_4_js_test.js
```


* login_demo_4_js_test.js

```js
client.test("Request executed successfully", function () {
    client.assert(response.status === 200, "Response status is not 200");
    client.log("response.body=" + response.body);
    client.assert(response.body === 'you have been authenticated', "Response body check fail");
});
```


![](https://ws1.sinaimg.cn/large/006tKfTcgy1g0xnun0pfng30w20i57n3.gif)

