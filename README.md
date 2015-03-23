jjsg - Java JSON SMS Gateway
=====================

## Features
- [x] Ready for send SMS via SMSC with SMPP Protocol
- [x] Authentication System
- [x] JSON Bulk procesing SMS for requests and responses
- [x] Scheduled o Instant SMS's
- [x] ACK Sending

## Installation

1. Copy supplied gateway.properties in your /etc/jjsg/ folder
2. Edit gateway.properties setting the user name, password and other correct values
3. Deploy jjsg.war in your Servlet container

## Usage

### Making Requests
All requests must include a "json" field.

### Send an instant SMS
```json
{ "user": "amalio",
  "password": "secret",
  "sms_request": [
    {
      "msisdn": "34646974525",
      "sender": "sender_str",
      "text": "message text"
    }
  ]
}
```

### Send an instant SMS with ACK
```json
{ "user": "amalio",
  "password": "secret",
  "sms_request": [
    {
      "msisdn": "34646974525",
      "sender": "sender_str",
      "text": "message text",
      "subid": "subid1",
      "ack_url": "http://www.anurl.com/ack"
    }
  ]
}
```

### Schedule an SMS
```json
{ "user": "amalio",
  "password": "secret",
  "sms_request": [
    {
      "msisdn": "34646974525",
      "sender": "sender_str",
      "text": "message text",
      "datetime": "2015-12-27 10:00"
    }
  ]
}
```

### Updating an scheduled SMS
```json
{ "user": "amalio",
  "password": "secret",
  "sms_request": [
    {
      "id": "subid1",
      "msisdn": "34646974525",
      "sender": "sender_str",
      "text": "message text",
      "datetime": "2015-12-27 11:00"
    }
  ]
}
```

### Delete an scheduled SMS
```json
{ "user": "amalio",
  "password": "secret",
  "sms_request": [
    {
      "id": "subid1",
      "forDelete": true
    }
  ]
}
```

### Making several requests
```json
{ 
"user": "amalio",
"password": "secret",
"sms_request": [
    {
      "id": 0,
      "msisdn": "34646974525",
      "sender": "sender_str",
      "text": "the text of SMS with an ñ",
      "subid": "subid1",
      "ack_url": "http://www.opteral.com/ack",
      "datetime": null,
      "test": false
    },
    {
      "id": null,
      "msisdn": "34646974525",
      "sender": "sender_str",
      "text": "the text of SMS with an ñ",
      "datetime": "2015-12-27 10:00",
      "subid": "subid2",
      "ack_url": "http://www.opteral.com/ack",
      "test": false
    },
    {
      "id": 1,
      "msisdn": "34646974525",
      "sender": "sender_str",
      "text": "the text of SMS with an ñ",
      "datetime": "2015-12-27 11:00",
      "subid": "subid3",
      "ack_url": "http://www.opteral.com/ack",
      "test": false
    },
    {
      "msisdn": "34646974525",
      "sender": "sender_str",
      "text": "the text of SMS with an ñ",
      "datetime": null,
      "test": true,
      "forDelete": true
    }
  ]
}
```
### Parsing Responses
An OK response looks like this:  
```json
{
"response_code":"OK",
"sms_responses":[
    {
    "request_ok":true,
    "status":"ACCEPTD",
    "id":1201,
    "subid":"subid1"},
    {
    "request_ok":true,
    "status":"ACCEPTD",
    "id":1202,
    "subid":"subid2"
    }
  ]
}
```
If Authetication fails then response looks like this:  
```json
{
"response_code":"ERROR_LOGIN",
" msg":"Athetication failed"
}
```  
If gateway fails then response looks like this:  
```json
{
"response_code":"ERROR_GENERAL",
" msg":"an explanation message"
}
```  
If an SMS fails then response looks like this:  
```json
{
"response_code":"OK",
"sms_responses":[
    {
    "request_ok":true,
    "status":"REJECTD",
    "id":0,
    "subid":"subid1"
    },
    {
    "request_ok":true,
    "status":"ACCEPTD",
    "id":1202,
    "subid":"subid2"
    }
  ]
}
```  
## License

jjsg is released under the Unlicense. See LICENSE for details.