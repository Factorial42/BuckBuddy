# BuckBuddy Campaign Microservice REST API

## General notes:
Response structure will have "data"-holding campaign object for this service and "error"-holding any "message" specific to errors that occured in the backend elements.

## create regular campaign
Req:
curl -i -XPOST 'localhost:4567/campaigns/' -d '{"userId":"782159708e9bb3e3af5c9bdf1ff77f70823418d16970eb82fe4d7e1ca5ca69ac", "name":"testcampaign", "amount":12.12}'

Res:
HTTP/1.1 201 Created
Date: Sun, 17 Apr 2016 20:41:39 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"data":{"userId":"782159708e9bb3e3af5c9bdf1ff77f70823418d16970eb82fe4d7e1ca5ca69ac","campaignId":"24d49175-dc95-46d8-ad33-baa02b4c5cd5","createdAt":1460925699.704,"lastUpdatedAt":1460925699.706,"name":"testcampaign","amount":12.12}}

## get campaign
Req:
curl -i -XGET 'localhost:4567/campaigns/24d49175-dc95-46d8-ad33-baa02b4c5cd5'

Res:
HTTP/1.1 200 OK
Date: Sun, 17 Apr 2016 20:42:10 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"userId":"782159708e9bb3e3af5c9bdf1ff77f70823418d16970eb82fe4d7e1ca5ca69ac","campaignId":"24d49175-dc95-46d8-ad33-baa02b4c5cd5","createdAt":1460925699.704000000,"lastUpdatedAt":1460925699.706000000,"name":"testcampaign","amount":12.12}

## delete campaign
Req:
curl -i -XDELETE 'localhost:4567/campaigns/24d49175-dc95-46d8-ad33-baa02b4c5cd5' 

Res:
HTTP/1.1 204 No Content
Date: Sun, 17 Apr 2016 20:42:47 GMT
Content-Type: application/json
Server: Jetty(9.3.2.v20150730)

## upload pic for a campaign
Req:
curl -i -XPOST 'localhost:4567/campaigns/24d49175-dc95-46d8-ad33-baa02b4c5cd5/uploadProfilePic?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhMzczMWQyYmQ2ZjJmY2YxMjllNGRmZmZmNDA1NTc5N2RiZTljYWNjYzI1NTc2ZjYyNTYxMjJiZjBhYzM0NTlmIn0.x4kBC19Z2jaU-DhnlqTJ5M000B0BhC454jhFl0GDc4jB6pAm9GT7wQWbS4dzwBSP6V-W5E4BbFO8A_DpbKbDng' -F "image=@/Users/jtandalai/Downloads/Fresh_Brothers_65x35.png"

Res:
TTP/1.1 200 OK
Date: Mon, 18 Apr 2016 17:48:36 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"data":{"campaignId":"c657239f-046f-42ef-8ccc-cb1b6c1c950b","profilePics":[{"sequence":0,"url":"http://user.assets.dev.buckbuddy.com/c657239f-046f-42ef-8ccc-cb1b6c1c950b/campaign.pic/Fresh_Brothers_65x35.png"},{"sequence":0,"url":"http://user.assets.dev.buckbuddy.com/c657239f-046f-42ef-8ccc-cb1b6c1c950b/campaign.pic/Fresh_Brothers_65x35-2.png"}],"userId":"a3731d2bd6f2fcf129e4dffff4055797dbe9caccc25576f6256122bf0ac3459f"}}
