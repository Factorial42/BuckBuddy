# BuckBuddy Campaign Microservice REST API

## General notes:
Response structure will have "data"-holding campaign object for this service and "error"-holding any "message" specific to errors that occured in the backend elements.

## create campaign
Req:
curl -i -XPOST 'localhost:4568/campaigns/' -d '{"userId":"782159708e9bb3e3af5c9bdf1ff77f70823418d16970eb82fe4d7e1ca5ca69ac", "name":"testcampaign", "amount":12.12, "userSlug":  "test-user-1461137733"}'

Res:
HTTP/1.1 201 Created
Date: Sun, 17 Apr 2016 20:41:39 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"data":{"userId":"782159708e9bb3e3af5c9bdf1ff77f70823418d16970eb82fe4d7e1ca5ca69ac","campaignId":"24d49175-dc95-46d8-ad33-baa02b4c5cd5","createdAt":1460925699.704,"lastUpdatedAt":1460925699.706,"name":"testcampaign","amount":12.12,"contributorsCount":0,"active":false,"days":0,"campaignSlug":"testcampaign-1461143741"}}

## get campaign by campaign Id
Req:
curl -i -XGET 'localhost:4568/campaigns/24d49175-dc95-46d8-ad33-baa02b4c5cd5'

Res:
HTTP/1.1 200 OK
Date: Sun, 17 Apr 2016 20:42:10 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"userId":"782159708e9bb3e3af5c9bdf1ff77f70823418d16970eb82fe4d7e1ca5ca69ac","campaignId":"24d49175-dc95-46d8-ad33-baa02b4c5cd5","createdAt":1460925699.704000000,"lastUpdatedAt":1460925699.706000000,"name":"testcampaign","amount":12.12,"contributorsCount":0,"active":false,"days":0,"campaignSlug":"testcampaign-1461143741"}


## get campaign by campaign Id MINIFIED
Req:
curl -i -XGET 'localhost:4568/campaigns/24d49175-dc95-46d8-ad33-baa02b4c5cd5/minified'

Res:
HTTP/1.1 200 OK
Date: Sun, 17 Apr 2016 20:42:10 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"name":"testcampaign", "description": "", "amount":12.12, "profilePics":[]}


## get campaign by token
Req:
curl -i -XGET 'localhost:4568/campaigns/byToken/eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhMzczMWQyYmQ2ZjJmY2YxMjllNGRmZmZmNDA1NTc5N2RiZTljYWNjYzI1NTc2ZjYyNTYxMjJiZjBhYzM0NTlmIn0.x4kBC19Z2jaU-DhnlqTJ5M000B0BhC454jhFl0GDc4jB6pAm9GT7wQWbS4dzwBSP6V-W5E4BbFO8A_DpbKbDng'

Res:
HTTP/1.1 200 OK
Date: Mon, 18 Apr 2016 19:05:10 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"data":{"userId":"a3731d2bd6f2fcf129e4dffff4055797dbe9caccc25576f6256122bf0ac3459f","campaignId":"c7e154ff-839c-4bab-a2e7-b988654d384d","createdAt":1461006282.188,"lastUpdatedAt":1461006282.191,"name":"testcampaign","amount":12.12,"contributorsCount":0,"active":false,"days":0,"campaignSlug":"testcampaign-1461143741"}}

## get campaign by token MINIFIED
Req:
curl -i -XGET 'localhost:4568/campaigns/byToken/eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhMzczMWQyYmQ2ZjJmY2YxMjllNGRmZmZmNDA1NTc5N2RiZTljYWNjYzI1NTc2ZjYyNTYxMjJiZjBhYzM0NTlmIn0.x4kBC19Z2jaU-DhnlqTJ5M000B0BhC454jhFl0GDc4jB6pAm9GT7wQWbS4dzwBSP6V-W5E4BbFO8A_DpbKbDng/minified'

Res:
HTTP/1.1 200 OK
Date: Mon, 18 Apr 2016 19:05:10 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"name":"testcampaign", "description": "", "amount":12.12, "profilePics":[]}

## get campaign by slug
Req:
curl -i -XGET 'localhost:4568/campaigns/bySlug/testcampaign-1461143741?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlYzY3OTc4MzIyZTVmYmQ3NjA0ZThkYjJhZDBlODlhYjFmYzRiN2RlZTBhOWE5NjU1ZmI2NjJiOGFlNjQ3ZTcxIn0.gAarGBEnQf11oc8h2bfyaiLoBYGOiaj_Lrjb-KV_SL5GZECE7j50wegLkI7ea1RfNAZBhFFa6LV4IPJQ7I3rjA'

Res:
HTTP/1.1 200 OK
Date: Wed, 20 Apr 2016 21:41:13 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"userId":"ec67978322e5fbd7604e8db2ad0e89ab1fc4b7dee0a9a9655fb662b8ae647e71","userSlug":"test-user-1461137733","campaignSlug":"testcampaign-1461143741","campaignId":"4219321d-02a8-46f7-83d4-82bb3b85a639","createdAt":1461143741.520000000,"lastUpdatedAt":1461171228.924000000,"name":"testcampaign","amount":12.13,"contributorsCount":0,"active":false,"days":0,"campaignSlug":"testcampaign-1461143741"}

## get campaign by slug MINIFIED
Req:
curl -i -XGET 'localhost:4568/campaigns/bySlug/testcampaign-1461143741/minified?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlYzY3OTc4MzIyZTVmYmQ3NjA0ZThkYjJhZDBlODlhYjFmYzRiN2RlZTBhOWE5NjU1ZmI2NjJiOGFlNjQ3ZTcxIn0.gAarGBEnQf11oc8h2bfyaiLoBYGOiaj_Lrjb-KV_SL5GZECE7j50wegLkI7ea1RfNAZBhFFa6LV4IPJQ7I3rjA'

Res:
HTTP/1.1 200 OK
Date: Wed, 20 Apr 2016 21:41:13 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"name":"testcampaign", "description": "", "amount":12.12, "profilePics":[]}

## delete campaign
Req:
curl -i -XDELETE 'localhost:4568/campaigns/24d49175-dc95-46d8-ad33-baa02b4c5cd5' 

Res:
HTTP/1.1 204 No Content
Date: Sun, 17 Apr 2016 20:42:47 GMT
Content-Type: application/json
Server: Jetty(9.3.2.v20150730)

## upload pic for a campaign
Req:
curl -i -XPOST 'localhost:4568/campaigns/24d49175-dc95-46d8-ad33-baa02b4c5cd5/uploadProfilePic?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhMzczMWQyYmQ2ZjJmY2YxMjllNGRmZmZmNDA1NTc5N2RiZTljYWNjYzI1NTc2ZjYyNTYxMjJiZjBhYzM0NTlmIn0.x4kBC19Z2jaU-DhnlqTJ5M000B0BhC454jhFl0GDc4jB6pAm9GT7wQWbS4dzwBSP6V-W5E4BbFO8A_DpbKbDng&sequence=0' -F "image=@/Users/jtandalai/Downloads/Fresh_Brothers_65x35.png"

Res:
TTP/1.1 200 OK
Date: Mon, 18 Apr 2016 17:48:36 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"data":{"userId":"ec67978322e5fbd7604e8db2ad0e89ab1fc4b7dee0a9a9655fb662b8ae647e71","userSlug":"test-user-1461137733","campaignSlug":"testcampaign-1461143741","campaignId":"4219321d-02a8-46f7-83d4-82bb3b85a639","createdAt":1461143741.52,"lastUpdatedAt":1461171198.626,"name":"testcampaign","amount":12.13,"contributorsCount":0,"active":false,"days":0,"profilePics":[{"sequence":1,"profilePicId":"fca2b2af49a74c85dbe803cb7ee4045fa4672b48bd89e9343c177158746100d3","url":"http://user.assets.dev.buckbuddy.com/ec67978322e5fbd7604e8db2ad0e89ab1fc4b7dee0a9a9655fb662b8ae647e71/campaign.pic/4219321d-02a8-46f7-83d4-82bb3b85a639/Fresh_Brothers_65x35.png"}],"campaignSlug":"testcampaign-1461143741"}}

## delete pic for a campaign
Req:
curl -i -XDELETE 'localhost:4568/campaigns/4219321d-02a8-46f7-83d4-82bb3b85a639/profilePic/439a6c90baa1cc8f9214cd4cf7ab6f1c7a075601222ee6c19f3528997d0485?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlYzY3OTc4MzIyZTVmYmQ3NjA0ZThkYjJhZDBlODlhYjFmYzRiN2RlZTBhOWE5NjU1ZmI2NjJiOGFlNjQ3ZTcxIn0.gAarGBEnQf11oc8h2bfyaiLoBYGOiaj_Lrjb-KV_SL5GZECE7j50wegLkI7ea1RfNAZBhFFa6LV4IPJQ7I3rjA'

Res:
HTTP/1.1 200 OK
Date: Wed, 20 Apr 2016 16:49:59 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"data":{"userId":"ec67978322e5fbd7604e8db2ad0e89ab1fc4b7dee0a9a9655fb662b8ae647e71","userSlug":"test-user-1461137733","campaignSlug":"testcampaign-1461143741","campaignId":"4219321d-02a8-46f7-83d4-82bb3b85a639","createdAt":1461143741.52,"lastUpdatedAt":1461171228.924,"name":"testcampaign","amount":12.13,"contributorsCount":0,"active":false,"days":0,"campaignSlug":"testcampaign-1461143741"}}
