youdian单词后端接口文档

<http://youdian.asedrfa.top>

<http://youdian.asedrfa.top/swagger-ui.html>

获取验证码：http://youdian.asedrfa.top/getCodeImage

测试账户

leeson

leeson



## 0 错误代码

| state | 错误类型               |
| :---: | :--------------------- |
|  100  | 请求者应当继续提出请求 |
|  101  | 未登录                 |
|  200  | 请求成功并返回         |
|       |                        |
|  400  | 请求参数出现空值       |
|  401  | 请求参数异常           |
|  402  | 验证码错误             |
|       |                        |
|       |                        |
|  410  | 账户不存在             |
|  411  | 密码错误               |
|  412  | 账户已存在             |
|       |                        |
|  420  | token非法              |
|  421  | token失效              |
|  422  | token过期              |
|  500  | 服务器异常             |
|       |                        |
|       |                        |



## 1. 用户信息

```sql
#  1. 用户

#  id	 昵称  电话 密码	描述	 邮箱  微信  年级	 生日	  创建时间   头像	  背景    手机提醒  手表提醒
# u_id  name  tel  pwd  desc email  wx  grade birth  c_date  h_url  b_url   pho_rem  wat_rem

# 2. 登陆记录

# 用户id  设备标识  设备类型  token    ip
# u_id   dev_id    tag     token    ip
```

### 1.1 登录

#### 1.1.1 手机登录(手机号)

|  接口链接  | 请求方式 | Header |
| :--------: | :------: | :----: |
| /user/load |   POST   |        |

请求参数:form

| 参数名 | 必要 | 描述                             | 类型       | 示例           |
| ------ | ---- | -------------------------------- | ---------- | -------------- |
| tel    | 1    | 电话号码                         | string(20) | 18523678963    |
| pwd    | 1    | 密码                             | string(20) | agdcvqbhbaj    |
| devId  | 1    | 设备标识（1:手机,2:手表,0:前端） | string(45) | 0dhvyb-sjvb-as |

响应结果：json

| 状态码state | 消息message | 响应数据data       |
| ----------- | ----------- | ------------------ |
| 200         | 登陆成功    | "token":"dajvasbb" |
| 410         | 账户不存在  |                    |
| 411         | 密码错误    |                    |

响应成功json示例：

```json
{
    "state":200,
    "message":"登陆成功",
    "data":{
        "token":"jdsvbavbauvbvwavejvbadsv"
    }
}
```

#### 1.1.2 手表、Web登录

手表登录： 

​        手表生成自己的devId,第一次form发送给后端，后端接收后返回一个token（101表示未登录）

​		然后计时五分钟内循环请求（只需要带上token），如果手机扫码登录，表示登陆成功（返回200和新的token）。

​		五分钟后重新发送devId

|  接口链接   | 请求方式 | Header |
| :---------: | :------: | :----: |
| /user/wload |   POST   | token  |

请求参数:form

| 参数名 | 必要 | 描述                             | 类型       | 示例           |
| ------ | ---- | -------------------------------- | ---------- | -------------- |
| devId  | 1    | 设备标识（1:手机,2:手表,0:前端） | string(45) | 2dhvyb-sjvb-as |

响应结果：json

| 状态码state | 消息message | 响应数据data       |
| ----------- | ----------- | ------------------ |
| 200         | 登陆成功    | "token":"dajvasbb" |
| 101         | 未登录      |                    |

响应成功json示例：

```json
{
    "state":200,
    "message":"登陆成功",
    "data":{
        "token":"jdsvbavbauvbvwavejvbadsv"
    }
}
```

#### 1.1.3 手机扫码登录

|  接口链接   | 请求方式 | Header |
| :---------: | :------: | :----: |
| /user/pload |   POST   | token  |

请求参数:form

| 参数名 | 必要 | 描述                             | 类型       | 示例           |
| ------ | ---- | -------------------------------- | ---------- | -------------- |
| devId  | 1    | 设备标识（1:手机,2:手表,0:前端） | string(45) | 0dhvyb-sjvb-as |

响应结果：json

| 状态码state | 消息message | 响应数据data |
| ----------- | ----------- | ------------ |
| 200         | 登陆成功    |              |
| 420/421/422 | 登录失败    |              |

响应成功json示例：

```json
{
    "state":200,
    "message":"登陆成功"
}
```



### 1.2 注册

|  接口链接   | 请求方式 | Header |
| :---------: | :------: | :----: |
| user/regist |   POST   |        |

请求参数form

| 参数名 | 必要 | 描述   | 类型       | 示例        |
| ------ | ---- | ------ | ---------- | ----------- |
| tel    | 1    | 电话   | string(20) | 18523678963 |
| pwd    | 1    | 密码   | string(20) | 18523678963 |
| code   | 1    | 验证码 | string(6)  | 567522      |

code：先向服务器请求：http://youdian.asedrfa.top/getCodeImage 返回一张图片，用户根据图片识别文字，填写code。

响应参数

| 状态码state | 消息message | 响应数据data |
| ----------- | ----------- | ------------ |
| 200         | 注册成功    |              |
| 402         | 验证码错误  |              |
| 412         | 用户已注册  |              |
| 500         | 服务器错误  |              |

响应成功json示例：

```json
{
    "state":200,
    "message":"注册成功"
}
```



### 1.3 找回密码

|  接口链接  | 请求方式 | Header |
| :--------: | :------: | :----: |
| /user/find |   POST   |        |

请求参数form

| 参数名 | 必要 | 描述   | 类型       | 示例        |
| ------ | ---- | ------ | ---------- | ----------- |
| tel    | 1    | 电话   | string(20) | 18523678963 |
| pwd    | 1    | 密码   | string(20) | 18523678963 |
| code   | 1    | 验证码 | string(6)  | 567522      |

code：先向服务器请求：http://youdian.asedrfa.top/getCodeImage 返回一张图片，用户根据图片识别文字，填写code。

响应参数

| 状态码state | 消息message  | 响应数据data |
| ----------- | ------------ | ------------ |
| 200         | 找回密码成功 |              |
| 402         | 验证码错误   |              |
| 500         | 服务器错误   |              |

响应成功json示例：

```json
{
    "state":200,
    "message":"找回密码成功"
}
```



### 1.4 获取用户信息

```sql
#  id	 昵称  电话 密码	描述	 邮箱  微信  年级	 生日	  创建时间   头像	  背景    手机提醒  手表提醒
# u_id  name  tel  pwd  desc email  wx  grade birth  c_date  h_url  b_url   pho_rem  wat_rem
```

#### 1.4.1 获取用户详细详细

|   接口链接    | 请求方式 | Header |
| :-----------: | :------: | :----: |
| /user/getuser |   Post   | token  |

响应参数

| 状态码state | 消息message | 响应数据data |
| ----------- | ----------- | ------------ |
| 200         | 成功        | json         |
| 500         | 服务器错误  |              |

响应成功json示例：

```json
{
    "state":200,
    "message":"请求成功",
    "data":{
          "state": 200,
          "message": {
            "name": "asedrfa ",
            "desc": "asedrfa测试用户",
            "email": "asedrfa@163.com",
            "tel": "leeson",
            "pwd": "leeson",
            "wx": "",
            "grade": "大三",
            "birth": "2021-10-30T06:12:44.000+00:00",
            "cdate": "2021-10-30T06:12:44.000+00:00",
            "burl": "",
            "hurl": "",
            "uid": "5b40dd23-f26b-4866-ba50-8a61109d5c4c"
          }
    }
}
```

#### 1.4.2 获取用户主页信息

|      接口链接      | 请求方式 | Header |
| :----------------: | :------: | :----: |
| /user/getuserindex |   POST   | token  |

请求参数form

| 参数名 | 必要 | 描述   |
| ------ | ---- | ------ |
| uId    | 1    | 用户Id |



响应参数

| 状态码state | 消息message | 响应数据data |
| ----------- | ----------- | ------------ |
| 200         | 获取成功    | json         |
| 500         | 服务器错误  |              |

响应成功json示例：

```json
{
    
  "state": 200,
  "message": "获取成功",
  "data": {
    "name": "leeson",
    "desc": "",
    "uId": "2cce77b1-5dab-499d-82ea-6de6362b7425",
    "hUrl": "/download/users/img/d2853ebf-2f0f-4fc1-af9f-673e2e88f9eb.jpg",
    "bUrl": "/download/users/img/bdb7985d-77d3-4cbd-9e50-27f9c3cf3825.jpg"
  }
}
```



### 1.5 修改用户信息

#### 1.5.1 修改用户基本信息

|   接口链接   | 请求方式 | Header |
| :----------: | :------: | :----: |
| /user/update |   POST   | token  |

请求参数

| 参数名 | 必要 | 描述     | 类型        | 示例               |
| ------ | ---- | -------- | ----------- | ------------------ |
| name   | 0    | 姓名     | String(20)  | Leeson             |
| desc   | 0    | 用户描述 | String(100) | 大家好，我是。。。 |
| email  | 0    | 邮箱     | String(100) |                    |
| wx     | 0    | 微信     | String(100) |                    |
| grade  | 0    | 年级     | String(20)  |                    |
| birth  | 0    | 生日     | datetime    |                    |

响应参数

| 状态码state | 消息message | 响应数据data |
| ----------- | ----------- | ------------ |
| 200         | 成功        | json         |
| 500         | 服务器错误  |              |

响应成功json示例：

```json
{
    "state":200,
    "message":"请求成功"
}
```

#### 1.5.2 修改用户头像

|    接口链接     | 请求方式 | Header |
| :-------------: | :------: | :----: |
| user/changehead |   POST   | token  |

请求参数

| 参数名 | 必要 | 描述     | 类型         | 示例 |
| ------ | ---- | -------- | ------------ | ---- |
| head   | 1    | 头像文件 | （jpg、png） |      |

响应参数

| 状态码state | 消息message | 响应数据data |
| ----------- | ----------- | ------------ |
| 200         | 请求成功    | json         |
| 500         | 服务器错误  |              |

响应成功json示例：       

```json
{
    "state":200,
    "message":"请求成功",
    "data":{
        "hUrl":"/kvdsaba/advkhj.jpg"
    }
}
```

#### 1.5.3 修改用户背景图片

|    接口链接     | 请求方式 | Header |
| :-------------: | :------: | :----: |
| user/changeback |   POST   | token  |

请求参数

| 参数名 | 必要 | 描述     | 类型         | 示例 |
| ------ | ---- | -------- | ------------ | ---- |
| back   | 1    | 背景文件 | （jpg、png） |      |

响应参数

| 状态码state | 消息message | 响应数据data |
| ----------- | ----------- | ------------ |
| 200         | 请求成功    | json         |
| 500         | 服务器错误  |              |

响应成功json示例：       

```json
{
    "state":200,
    "message":"请求成功",
    "data":{
        "bUrl":"/kvdsaba/advkhj.jpg"
    }
}
```



## 2. 个人偏好设置

```sql
# 用户id  单词本 手机提醒  手表提醒     天数    几个list  每个List单词数量  循环方式
# u_id    tag  pho_rem  wat_rem   day_time  list    num_of_list    circ_way

# 单词本说明
# tag:   高中/CET4/CET6/考研/GRE/TOEFL/IELTS
```



### 2.1 修改个人偏好

|    接口链接     | 请求方式 | Header |
| :-------------: | :------: | :----: |
| user/setelement |   POST   | token  |

请求参数

| 参数名      | 必要 | 描述               | 类型       | 示例      |
| ----------- | ---- | ------------------ | ---------- | --------- |
| tag         | 1    | 单词本             | string(20) | 高中      |
| pho_rem     | 1    | 手机提醒           | 1/0        | 1         |
| wat_rem     | 1    | 手表               | 1/0        | 1         |
| day_time    | 0    | 预定记忆天数       | int        | 10        |
| list        | 0    | 多少个list         | int        | 4         |
| num_of_list | 0    | 一个list中的单词数 | int        | 10        |
| circ_way    | 1    | 循环方式           | int        | 0（默认） |

响应参数

| 状态码state | 消息message | 响应数据data |
| ----------- | ----------- | ------------ |
| 200         | 成功        | json         |
| 500         | 服务器错误  |              |

响应成功json示例：       

```json
{
    "state":200,
    "message":"请求成功"
}
```

### 2.2 获取个人偏好

|      接口链接      | 请求方式 | Header |
| :----------------: | :------: | :----: |
| user/getsetelement |   POST   | token  |

请求参数: token

响应参数

| 状态码state | 消息message | 响应数据data |
| ----------- | ----------- | ------------ |
| 200         | 成功        | json         |
| 500         | 服务器错误  |              |

响应成功json示例：       

```json
{
    "state":200,
    "message":"请求成功",
    "data": {
        "id": "4bd0073e3a2711ec8191005056c00001",
        "tag": "四级",
        "phoRem": 1,
        "watRem": 1,
        "dayTime": 40,
        "list": 4,
        "numOfList": 10,
        "circWay": 0,
        "uid": "16780094-3e85-4b68-9c9a-489d8fbcb887"
    }
}
```





## 3. 用户单词库

总共五个库（放在一个表，使用tag区分）

```sql
#      id       用户id    单词id   标记      复习次数            难度      添加时间
#  用户单词id     u_id      w_id    tag    times_review      difficult   c_date
```

| 单词库名称     | tag  | 说明                                                         |
| -------------- | ---- | ------------------------------------------------------------ |
| 陌生单词库     | 0    | 用户初始化时，默认10*10个(单词类别随机)，后面设置了个人偏好的tag，就自动更换单词类别 |
| 无需再背       | 1    | 用户手动添加（apple，this）                                  |
| 熟悉单词       | 2    | 连续对了三颗星                                               |
| 三次全错的收藏 | 3    | 三次全错                                                     |
| 手动添加的收藏 | 4    | 手动添加，用户认为有必要                                     |

difficult:

| difficult | 说明           |
| --------- | -------------- |
| 0         | 默认           |
| 1         | 斩 -> 无需再背 |
| 2         | 3绿0红         |
| 3         | 3：1           |
| 4         | 3：2           |
| 5         | 2：3           |
| 6         | 1：3           |
| 7         | 0：3           |
| 8         | 手动添加收藏   |



### 3.1 获取陌生单词列表

|   接口链接   | 请求方式 | Header |
| :----------: | :------: | :----: |
| user/getlist |   POST   | token  |

响应参数

| 状态码state | 消息message | 响应数据data |
| ----------- | ----------- | ------------ |
| 200         | 成功        | json         |
| 500         | 服务器错误  |              |

响应成功json示例：       

```json
{
    "state":200,
    "message":"请求成功",
    "data":[
        {
      "id": "52ef9d65-3af7-11ec-8191-005056c00001",
      "tag": 0,
      "timesReview": 0,
      "difficult": 0,
      "word": {
        "spell": "thingamabob",
        "tag": "0",
        "href": "http://dict.cn/thingamabob",
        "audio": [
          {
            "tag": "英音：",
            "tagDetail": "/'ætɪtjuːd/",
            "url": "https://api.frdic.com/api/v2/speech/speakweb?langid=en&voicename=en_uk_male&txt=QYNYXR0aXR1ZGU%3d"
          },
          {
            "tag": "美音：",
            "tagDetail": "/'ætɪtʊd/",
            "url": "https://api.frdic.com/api/v2/speech/speakweb?langid=en&voicename=en_us_female&txt=QYNYXR0aXR1ZGU%3d"
          }
        ],
        "clearfix": [
          {
            "clearfix": "n.<口>（用以指某人或某事）某人，某件东西"
          }
        ],
        "deformation": [
          {
            "tag": "异体字:",
            "tagList": "thingumabob"
          }
        ],
        "phrase": [],
        "sentences": [],
        "similars": [
          {
            "tag": "【近义词】",
            "tagList": "thing+thingmajig+gimmick+doodad+whatchamacallit"
          }
        ],
        "wid": "2UQXDNU4EOWZ5UO720Z711255EMNHFYVMFNU2RY"
      },
      "wid": "2UQXDNU4EOWZ5UO720Z711255EMNHFYVMFNU2RY"
    }
    ]
      
}
```

### 3.2 提交背诵数据

```sql
#      id       用户id    单词id   标记      复习次数            难度      添加时间
#  用户单词id     u_id      w_id    tag    times_review      difficult   c_date

# 4. 时间数据记录
#
# 用户id      日期   A  B  C  D  E  F  G  H  I  J  K  L  当日总时长   当日总个数
# u_id       day    A  B  C  D  E  F  G  H  I  J  K  L  sum_time      sum
```

|   接口链接    | 请求方式 | Header |
| :-----------: | :------: | :----: |
| user/postdata |   POST   | token  |

请求参数

| 参数名      | 必要 | 描述                                                     | 类型       | 示例                                                         |
| ----------- | ---- | -------------------------------------------------------- | ---------- | ------------------------------------------------------------ |
| id          | 1    | 用户单词id                                               | string(50) | "sadjvsgdja h "                                              |
| tag         | 1    | 用户单词库类型                                           | Integer    | 0:生词本 ，1：无需再背 ，2.熟悉单词，3：三次全错，4:手动添加的收藏 |
| timesReview | 1    | 复习次数                                                 | Integer    | 1                                                            |
| difficult   | 1    | 难度                                                     | Integer    | 见 3 difficult 表                                          |
| cDate       | 1    | 改为背诵完成时间（时间戳）                               | datetime   | 1635786994219                                                |
| day         | 1    | 哪一天（当日凌晨零点时间戳）                             | datetime   | 1635782400000                                                |
| sumTime     | 1    | 这天未上传的时间（新加多少时间，单位分钟）（后台作加法） | integer    | 8                                                            |

请求示例 json格式

```json
{
    "word_record":[
        {
            "id":"14bfaf8e-3b36-11ec-8191-005056c00001",
            "tag":1,
            "timesReview":1,
            "difficult":1,
            "cDate":"1635786994219"   
         },{
            "id":"14bfb2a4-3b36-11ec-8191-005056c00001",
            "tag":1,
            "timesReview":1,
            "difficult":1,
            "cDate":"1635786994219"   
         },{
            "id":"14bfb1c4-3b36-11ec-8191-005056c00001",
            "tag":3,
            "timesReview":0,
            "difficult":5,
            "cDate":"1635786994219"              
         }
    ],
    "time_record":[
        {
            "day":"1635782400000",
            "sumTime":20
        }
    ]
}
```

注意*

```txt
//总结构：
{
	"word_record":[],
    "time_record":[]
}

//其中"word_record"列表中的为：
{
    "id":"14bfaf8e-3b36-11ec-8191-005056c00001",
    "tag":1,
    "timesReview":1,
    "difficult":1,
    "cDate":"1635786994219"   
}

//"time_record"列表中的为：
{
    "day":"1635782400000",
    "sumTime":20
}

//请根据请求参数中的数据类型上传

```



响应参数

| 状态码state | 消息message | 响应数据data |
| ----------- | ----------- | ------------ |
| 200         | 成功        | json         |
| 500         | 服务器错误  |              |

响应成功json示例：       

```json
{
    "state":200,
    "message":"请求成功"
}
```



### 3.3  获取个人单词库

收藏、熟悉、无需再背的

|    接口链接    | 请求方式 | Header |
| :------------: | :------: | :----: |
| user/getmylist |   POST   | token  |

请求参数form

| 参数名 | 必要 | 描述       | 类型    | 示例                                                         |
| ------ | ---- | ---------- | ------- | ------------------------------------------------------------ |
| tag    | 1    | 用户单词库 | Integer | 0:生词本 ，1：无需再背 ，2.熟悉单词，3：三次全错，4:手动添加的收藏 |

响应参数

| 状态码state | 消息message | 响应数据data |
| ----------- | ----------- | ------------ |
| 200         | 成功        | json         |

响应成功json示例： 

```json
{
    "state":200,
    "message":"请求成功",
    "data":[
    {
      "id": "14bfaf8e-3b36-11ec-8191-005056c00001",
      "tag": 1,
      "timesReview": 1,
      "difficult": 1,
      "word": {
        "spell": "Cokeville",
        "tag": "0",
        "href": "http://dict.cn/Cokeville",
        "audio": [
          {
            "tag": "英音：",
            "tagDetail": "/'ætɪtjuːd/",
            "url": "https://api.frdic.com/api/v2/speech/speakweb?langid=en&voicename=en_uk_male&txt=QYNYXR0aXR1ZGU%3d"
          },
          {
            "tag": "美音：",
            "tagDetail": "/'ætɪtʊd/",
            "url": "https://api.frdic.com/api/v2/speech/speakweb?langid=en&voicename=en_us_female&txt=QYNYXR0aXR1ZGU%3d"
          }
        ],
        "clearfix": [
          {
            "clearfix": "[地名] [美国] 科克维尔"
          }
        ],
        "deformation": [],
        "phrase": [],
        "sentences": [],
        "similars": [],
        "wid": "WDZPXVLFA5OXGX9ET8B3CU7KNZGIZPOL7OSADRB"
      },
      "uid": "2cce77b1-5dab-499d-82ea-6de6362b7425",
      "cdate": "2021-11-01T17:16:34.000+00:00",
      "wid": "WDZPXVLFA5OXGX9ET8B3CU7KNZGIZPOL7OSADRB"
    },
    {
      "id": "14bfb2a4-3b36-11ec-8191-005056c00001",
      "tag": 1,
      "timesReview": 1,
      "difficult": 1,
      "word": {
        "spell": "rookies",
        "tag": "0",
        "href": "http://dict.cn/rookies",
        "audio": [
          {
            "tag": "英音：",
            "tagDetail": "/'ætɪtjuːd/",
            "url": "https://api.frdic.com/api/v2/speech/speakweb?langid=en&voicename=en_uk_male&txt=QYNYXR0aXR1ZGU%3d"
          },
          {
            "tag": "美音：",
            "tagDetail": "/'ætɪtʊd/",
            "url": "https://api.frdic.com/api/v2/speech/speakweb?langid=en&voicename=en_us_female&txt=QYNYXR0aXR1ZGU%3d"
          }
        ],
        "clearfix": [
          {
            "clearfix": "n.新成员，新手( rookie的名词复数 )"
          }
        ],
        "deformation": [],
        "phrase": [],
        "sentences": [],
        "similars": [],
        "wid": "PO26299KSARP3AP8LCLCJZYFAX0V4KB91F64U0O"
      },
      "uid": "2cce77b1-5dab-499d-82ea-6de6362b7425",
      "cdate": "2021-11-01T17:16:34.000+00:00",
      "wid": "PO26299KSARP3AP8LCLCJZYFAX0V4KB91F64U0O"
    }
  ]
}
```

### 3.4 获取背诵记录

|    接口链接    | 请求方式 | Header |
| :------------: | :------: | :----: |
| user/getrecord |   POST   | token  |

响应参数

| 状态码state | 消息message | 响应数据data |
| ----------- | ----------- | ------------ |
| 200         | 成功        | json         |
| 201         | 错误        |              |
| 404         | 路径错误    |              |
| 500         | 服务器错误  |              |

data参数说明

| 参数名       | 类型    | 中文说明                 |
| ------------ | ------- | ------------------------ |
| A            | Integer | 0-2时段的单词平均个数    |
| B            | Integer | 2-4时段的单词平均个数    |
| 。。。       | Integer |                          |
| AverageTime  | Integer | 每天记忆单词用时（s）    |
| AverageWords | Integer | 每小时记忆单词量（个/h） |
| Efficiency   | Integer | 比上一周效率变化{}%      |

响应成功json示例：     

```json
{
  "data": {
    "A": 6,
    "B": 5,
    "C": 5,
    "D": 4,
    "E": 3,
    "F": 4,
    "G": 4,
    "H": 1,
    "I": 4,
    "J": 0,
    "K": 2,
    "L": 0,
    "AverageTime": 117,
    "AverageWords": 178,
    "Efficiency": 4
  },
  "state": 200,
  "message": "获取记录成功"
}
```





### 3.5 获取今日计划

|   接口链接   | 请求方式 | Header |
| :----------: | :------: | :----: |
| user/getplan |   POST   | token  |

响应参数

| 状态码state | 消息message | 响应数据data |
| ----------- | ----------- | ------------ |
| 200         | 成功        | json         |
| 500         | 服务器错误  |              |

data参数说明

| 参数名     | 类型    | 中文说明                              |
| ---------- | ------- | ------------------------------------- |
| sumTime    | Integer | 用户注册到现在，背单词用的总时间（s） |
| efficiency | double  | 今日效率                              |
| today      | Integer | 今日新词                              |
| sumWord    | Integer | 用户注册到现在，背单词的总个数        |
| todayed    | Integer | 今日已背单词                          |
| toReviewed | Integer | 待复习                                |

响应成功json示例：   

```json
{
  "data": {
    "efficiency": 60.16713091922006,
    "sumTime": 3621,
    "today": 9,
    "sumWord": 100,
    "todayed": 6,
    "toReviewed": 6
  },
  "state": 200,
  "message": "获取今日计划成功"
}
```





## 4.获取发现页面







## 5. 查询单词

### 5.1 通过单词准确查找

|    接口链接    | 请求方式 | Header |
| :------------: | :------: | :----: |
| /word/queryall |   GET    |        |

请求参数 URI

| 参数名 | 必要 | 描述     | 类型   | 示例  |
| ------ | ---- | -------- | ------ | ----- |
| q      | 1    | 单词拼写 | String | apple |



### 5.2 通过翻译查询

|    接口链接    | 请求方式 | Header |
| :------------: | :------: | :----: |
| /word/queryall |   GET    |        |

请求参数 URI

| 参数名 | 必要 | 描述     | 类型   | 示例 |
| ------ | ---- | -------- | ------ | ---- |
| q      | 1    | 中文翻译 | String | 苹果 |

/word/queryall?q=苹果



### 5.3 通过单词模糊查找

|    接口链接    | 请求方式 | Header |
| :------------: | :------: | :----: |
| /word/queryall |   GET    |        |

请求参数 URI

| 参数名 | 必要 | 描述     | 类型    | 示例  |
| ------ | ---- | -------- | ------- | ----- |
| spell  | 1    | 单词拼写 | String  | apple |
| num    | 1    |          | Integer | 1     |

/word/queryall?spell=apple&num=1



### 5.4 获取单词本

通过tag查找

|    接口链接    | 请求方式 | Header |
| :------------: | :------: | :----: |
| /word/queryall |   GET    |        |

请求参数 URI

| 参数名 | 必要 | 描述   | 类型   | 示例                                |
| ------ | ---- | ------ | ------ | ----------------------------------- |
| tag    | 1    | 单词本 | String | 高中/CET4/CET6/考研/GRE/TOEFL/IELTS |

/word/queryall?tag=高中 



### 5.5 获取单词本中单词的数量

通过tag查找

|      接口链接       | 请求方式 | Header |
| :-----------------: | :------: | :----: |
| /word/queryNumByTag |   GET    |        |

请求参数 URI

| 参数名 | 必要 | 描述   | 类型   | 示例                                |
| ------ | ---- | ------ | ------ | ----------------------------------- |
| tag    | 1    | 单词本 | String | 高中/CET4/CET6/考研/GRE/TOEFL/IELTS |

请求实例

/word/queryNumByTag?tag=高中



响应实例

```json
{
  "data": {
    "num": 6774,
    "tag": "CET4"
  },
  "state": 200,
  "message": "请求成功"
}
```



## 二、 数据库设计

[E-R图]: http://youdian.asedrfa.top/img/ER图.png	"http://youdian.asedrfa.top/img/ER图.png"

如图所示五个表，修改对应信息就修改对应表



### 1. 单词的数据库

spell   tag    clearfix   href  word_deformation  phrase   sentences   audio        similar  difficult

拼写  标签  词性翻译  链接                变形              短语         例句列表  单词发音      近义词    难度

str       str        list        str                  list                 list               list            list                list        int

​               [{tag  tagList}]         [{key  value}]  [{key  value}]  [{tag tagDetail url}] [{tag tagList}]

```sql
drop database vocabulary;
create database vocabulary;
use vocabulary;

create table word
(
    w_id      varchar(50)  not null PRIMARY KEY COMMENT '单词id' default '',
    spell     varchar(100) not null COMMENT '拼写'               default '',
    tag       varchar(100) COMMENT '单词标签'                      default '',
    href      varchar(300) COMMENT '单词链接'                      default '',
    difficult smallint COMMENT '难度' default 0,
    unique index index_w_id (w_id)
);

# 词性+翻译  list 拼接
create table word_clear
(
    w_id     varchar(50) not null COMMENT '单词id' default '',
    clearfix text        not null COMMENT '词性+翻译',
    FOREIGN KEY (w_id) REFERENCES word (w_id)
);

# 变型  list 拼接
create table word_def
(
    w_id     varchar(50) not null COMMENT '单词id'  default '',
    tag      varchar(20) not null COMMENT '变型tag' default '',
    tag_list text        not null COMMENT '例子',
    FOREIGN KEY (w_id) REFERENCES word (w_id)
);

# 短语
create table word_phrase
(
    w_id    varchar(50)  not null COMMENT '单词id' default '',
    `key`   varchar(300) not null COMMENT '短语拼写' default '',
    `value` varchar(300) not null COMMENT '短语翻译' default '',
    FOREIGN KEY (w_id) REFERENCES word (w_id)
);

# 例句
create table word_sentences
(
    w_id    varchar(50) not null COMMENT '单词id' default '',
    `key`   text        not null COMMENT '句子英文',
    `value` text        not null COMMENT '句子翻译',
    FOREIGN KEY (w_id) REFERENCES word (w_id)
);

# 发音
create table word_audio
(
    w_id         varchar(50)  not null COMMENT '单词id' default '',
    `tag`        varchar(20)  not null COMMENT '发音类型' default '',
    `tag_detail` varchar(100) not null COMMENT '音标'   default '',
    `url`        varchar(300) COMMENT '语音链接'          default '',
    FOREIGN KEY (w_id) REFERENCES word (w_id)
);

# 相似词
create table word_similar
(
    w_id       varchar(50)  not null COMMENT '单词id' default '',
    `tag`      varchar(20)  not null COMMENT '相似类型' default '',
    `tag_list` varchar(200) not null COMMENT '例子'   default '',
    FOREIGN KEY (w_id) REFERENCES word (w_id)
);


```



### 2. 悠点数据库

```mysql
drop database youdian;
create database youdian;
use youdian;

#  1. 用户

#  id	 昵称  电话 密码	描述	 邮箱  微信 年级	生日	  创建时间   头像	  背景    手机提醒  手表提醒
# u_id  name  tel  pwd  desc email wx grade birth  c_date  h_url  b_url   pho_rem  wat_rem
create table user
(
    u_id   varchar(50)  not null PRIMARY KEY COMMENT '用户id' default '',
    name   varchar(20)  not null COMMENT '昵称'               default '',
    tel    varchar(20)  not null COMMENT '电话'               default '',
    pwd    varchar(20)  not null COMMENT '密码'               default '',
    `desc` varchar(100) not null COMMENT '描述'               default '',
    email  varchar(100) not null COMMENT '邮箱'               default '',
    wx     varchar(100) not null COMMENT '微信'               default '',
    grade  varchar(20)  not null COMMENT '年级'               default '',
    birth  datetime     not null COMMENT '生日'               default NOW(),
    c_date datetime     not null COMMENT '创建时间'             default NOW(),
    h_url  varchar(300) not null COMMENT '头像url'            default '',
    b_url  varchar(300) not null COMMENT '背景url'            default '',
    unique index index_u_id (u_id)
);

# 2. 登陆记录
#
# 用户id  设备标识  设备类型  token    ip
# u_id   dev_id    tag     token    ip
create table user_load
(
    dev_id varchar(50) not null primary key COMMENT '设备id' default '',
    u_id   varchar(50) not null COMMENT '用户id'             default '',
    tag    smallint    not null comment '设备类型'             default 0,
    token  text        not null COMMENT 'token',
    ip     varchar(26) not null COMMENT 'ip'               default '',
    foreign key (u_id) references user (u_id)
);

# 3.  用户单词库
#
# 标记说明
# tag:用户单词库
# tag：0 陌生词库（100个）
# tag: 1 无需再背
# tag: 3 收藏->三次全错
# tag: 4 收藏->手动添加
#
#      id       用户id    单词id   标记      复习次数            难度      添加时间
#  用户单词id     u_id      w_id    tag    times_review      difficult   c_date
create table user_word
(
    id           varchar(50) not null primary key COMMENT 'id' default '',
    u_id         varchar(50) not null COMMENT '用户id'           default '',
    w_id         varchar(50) not null COMMENT '单词id'           default '',
    tag          smallint    not null COMMENT '分类标记'           default 0,
    times_review smallint    not null COMMENT '是否复习'           default 0,
    difficult    smallint    not null COMMENT '难度'        default 0,
    c_date       datetime    not null COMMENT '创建时间'           default NOW(),
    foreign key (u_id) references user (u_id)
);

# 4. 时间数据记录
#
# 用户id      日期   A  B  C  D  E  F  G  H  I  J  K  L  当日总时长  当日总个数
# u_id       day    A  B  C  D  E  F  G  H  I  J  K  L  sum_time      sum
create table user_time_record
(
    id       varchar(50) not null primary key COMMENT 'id' default '',
    u_id     varchar(50) not null COMMENT '用户id'           default '',
    day      datetime    not null COMMENT '日期'             default NOW(),
    A        smallint    not null COMMENT '0-2'            default 0,
    B        smallint    not null COMMENT '2-4'            default 0,
    C        smallint    not null COMMENT '4-6'            default 0,
    D        smallint    not null COMMENT '6-8'            default 0,
    E        smallint    not null COMMENT '8-10'           default 0,
    F        smallint    not null COMMENT '10-12'          default 0,
    G        smallint    not null COMMENT '12-14'          default 0,
    H        smallint    not null COMMENT '14-16'          default 0,
    I        smallint    not null COMMENT '16-18'          default 0,
    J        smallint    not null COMMENT '18-20'          default 0,
    K        smallint    not null COMMENT '20-22'          default 0,
    `L`      smallint    not null COMMENT '22-24'          default 0,
    sum_time smallint    not null COMMENT '当日总时间'          default 0,
    sum      smallint    not null COMMENT 'sum'            default 0,
    foreign key (u_id) references user (u_id)
);

# 5. 提醒设置
#
# 用户id  单词本 手机提醒  手表提醒     天数    几个list  每个List单词数量  循环方式
# u_id    tag  pho_rem  wat_rem   day_time  list    num_of_list    circ_way
create table user_remind
(
    id          varchar(50) not null primary key COMMENT 'id' default '',
    u_id        varchar(50) not null COMMENT '用户id'           default '',
    tag         varchar(20) not null comment '单词本'            default '',
    pho_rem     boolean     not null COMMENT '手机提醒'           default 0,
    wat_rem     boolean     not null COMMENT '手表提醒'           default 0,
    day_time    int comment '天数'                              default 0,
    list        int comment '多少个list'                         default 0,
    num_of_list int comment '每个list单词数量'                      default 0,
    circ_way    smallint comment '循环方式'                       default 0,
    foreign key (u_id) references user (u_id)
);



# 6. 朋友圈（说说）
#
# 朋友圈 id  用户id   文字内容(500字）  获赞数    收藏数     评论
# c_id       u_id     contents       like     coll    comments
create table circle
(
    c_id       varchar(50)  not null PRIMARY KEY COMMENT '朋友圈id' default '',
    u_id       varchar(50)  not null COMMENT '用户id'              default '',
    contents   varchar(500) not null COMMENT '内容'                default '',
    `like`     int          not null comment '获赞数'               default 0,
    `coll`     int          not null comment '收藏数'               default 0,
    `comments` int          not null comment '评论数'               default 0,
    foreign key (u_id) references user (u_id)
);

# 7. 朋友圈图片内容
#
# 朋友圈id	图片链接
# c_id      photo_url
create table c_photo
(
    id        varchar(50)  not null primary key COMMENT 'id' default '',
    c_id      varchar(50)  not null COMMENT '朋友圈id'          default '',
    photo_url varchar(300) not null comment '图片链接'           default '',
    foreign key (c_id) references circle (c_id)
);

# 8. 赞
#
# 朋友圈id  赞用户id
# c_id      u_id
create table c_like
(
    id   varchar(50) not null primary key COMMENT 'id' default '',
    c_id varchar(50) not null COMMENT '朋友圈id'          default '',
    u_id varchar(50) not null COMMENT '用户id'           default '',
    foreign key (c_id) references circle (c_id),
    foreign key (u_id) references user (u_id)
);

# 9. 收藏
#
# 朋友圈id   收藏用户id
# c_id        u_id
create table c_fav
(
    id   varchar(50) not null primary key COMMENT 'id' default '',
    c_id varchar(50) not null COMMENT '朋友圈id'          default '',
    u_id varchar(50) not null COMMENT '用户id'           default '',
    foreign key (c_id) references circle (c_id),
    foreign key (u_id) references user (u_id)
);

# 10. 评论
#
# 朋友圈id  评论用户id   评论内容    评论时间
# c_id       u_id     comments  comment_date
create table c_comments
(
    id            varchar(50)  not null primary key COMMENT 'id' default '',
    c_id          varchar(50)  not null COMMENT '朋友圈id'          default '',
    u_id          varchar(50)  not null COMMENT '用户id'           default '',
    comments      varchar(200) not null comment '评论内容'           default '',
    comments_date datetime     not null comment '评论时间'           default NOW(),
    foreign key (c_id) references circle (c_id),
    foreign key (u_id) references user (u_id)
);

# 11 关注
# 用户id  被关注id 时间
create table follow
(
    id          varchar(50) not null primary key COMMENT 'id' default '',
    u_id        varchar(50) not null COMMENT '用户id'           default '',
    follow_id   varchar(50) not null COMMENT '被关注用户id'        default '',
    follow_date datetime    not null comment '关注时间'           default NOW()
);

drop procedure if exists update_user_word;
DELIMITER $$
create procedure update_user_word(
    in tag varchar(100), # 单词类型
    in uId varchar(50) # 用户id
)
begin
    declare count int;
    # 查找数量 用户单词库
    select count(*)
    from youdian.user_word as uword
    where uword.tag = 0 # tag = 0 表示陌生词库
      and u_id = uId
    into count;
    set count = 100 - count;

    if count > 0 then
        insert youdian.user_word(id, u_id, w_id, tag, times_review, difficult, c_date)
        select UUID(), uId, word.w_id, 0, 0, 0, NOW()
        from vocabulary.word as word
        where word.tag like concat('%', tag, '%')
          and word.w_id not in ( # 不重复
            select uword.w_id
            from youdian.user_word as uword
            where u_id = uId)
        order by rand()
        LIMIT count;

    END if;
end $$
DELIMITER ;




```



## 三、 悠点后端介绍

### 1. 概述





### 2. 配置环境

#### 2.1 开发环境

系统配置	CPU Intel(R) Core(TM) i5-8265U

​					内存 8.00 GB

​					储存 512 GB

操作系统	Window10 专业版

开发平台	Intellij IDEA、PyCharm 

开发环境    Jdk 1.8、Python 3.8.5、MySQL 5.7、

技术栈	SpringBoot 2.5.5、MyBatis 2.1.1、Requets2.26、BeautifulSoup 4.10.1

#### 2.1 服务器环境

系统配置	CPU：2核	内存：2G	带宽：1M

操作系统	Alibaba Cloud Linux 3.2104 64为位

运行环境	Jdk1.8、Python3.6.8、MySQL 5.7

软件	Ngnix 1.20







