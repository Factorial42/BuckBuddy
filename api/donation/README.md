# BuckBuddy Campaign Microservice REST API

## create donation
Req:
curl -i -XPOST 'localhost:4569/donations' -d '{"userSlug":"test-user-1461137856", "campaignSlug":"testcampaign-1461734320", "amountInCents":100, "paymentToken":"tok_184yPYHngV6Dzl2IwVR7ng1w","currencyString":"usd", "firstName":"testuser1"}'

Res:
HTTP/1.1 200 OK
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.0.2.v20130417)

{}

## get Paginated donations by campaignSlug
Req:
curl -i -XGET 'localhost:4569/donations/byCampaignSlug/testcampaign-1461734320?pageNumber=2&pageSize=4'

Res:
HTTP/1.1 200 OK
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.0.2.v20130417)
[{"donationId":"7055eb6d-01e2-409d-8435-58c6ae97bcab","createdAt":1461783699.087,"lastUpdatedAt":1461783699.087,"amountInCents":1E+2,"currencyString":"usd","paymentToken":"tok_184xkcHngV6Dzl2IPkh6gJC9","userSlug":"test-user-1461137856","campaignSlug":"testcampaign-1461734320","chargeUserResponse":{"nodeType":{}}},{"donationId":"60c9998c-70ca-479e-94f4-b5a8d84f26f3","createdAt":1461784079.826,"lastUpdatedAt":1461784079.826,"amountInCents":1E+2,"currencyString":"usd","paymentToken":"tok_184xywHngV6Dzl2IUWSHZt1i","userSlug":"test-user-1461137856","campaignSlug":"testcampaign-1461734320","chargeUserResponse":{"nodeType":{}}},{"donationId":"0f8fe0d9-3dce-45f5-88fb-9547ddb6f555","createdAt":1461784266.658,"lastUpdatedAt":1461784266.658,"amountInCents":1E+2,"currencyString":"usd","paymentToken":"tok_184y3CHngV6Dzl2IZkeNiMuF","userSlug":"test-user-1461137856","campaignSlug":"testcampaign-1461734320","chargeUserResponse":{"nodeType":{}}},{"donationId":"f48628b8-4f63-40d2-b4ad-4e1949e4587b","createdAt":1461784864.178,"lastUpdatedAt":1461784864.178,"amountInCents":1E+2,"currencyString":"usd","paymentToken":"tok_184yBxHngV6Dzl2IvIdeDb9P","userSlug":"test-user-1461137856","campaignSlug":"testcampaign-1461734320","chargeUserResponse":{"metadata":{},"livemode":false,"statementDescription":null,"statementDescriptor":null,"receiptEmail":null,"destination":"acct_184xd4JL16Rlwnz1","description":"","source":{"country":"US","last4":"4242","metadata":{},"dynamicLast4":null,"type":null,"expYear":2017,"fingerprint":"BCFPK6M2yYxYnvh0","addressLine1":null,"addressLine2":null,"currency":null,"id":"card_184yBxHngV6Dzl2IFA23TjfL","cvcCheck":"pass","brand":"Visa","addressCity":null,"addressLine1Check":null,"addressCountry":null,"funding":"credit","addressZip":null,"addressZipCheck":null,"addressState":null,"instanceURL":null,"expMonth":1,"tokenizationMethod":null,"name":null,"recipient":null,"account":null,"customer":null,"object":"card","status":null},"refunds":{"data":[],"count":null,"hasMore":false,"requestParams":null,"requestOptions":null,"totalCount":0,"url":"/v1/charges/ch_184yDGHngV6Dzl2IltNZrjVt/refunds"},"applicationFee":"fee_8LfY0t4C75ufrw","shipping":null,"amountRefunded":0,"captured":true,"balanceTransaction":"txn_184yDGHngV6Dzl2IQcZ1UJMT","currency":"usd","refunded":false,"id":"ch_184yDGHngV6Dzl2IltNZrjVt","outcome":null,"receiptNumber":null,"order":null,"dispute":null,"amount":62,"disputed":null,"failureCode":null,"created":1461784842,"transfer":"tr_184yDGHngV6Dzl2ImHfPO3kr","sourceTransfer":null,"paid":true,"invoice":null,"failureMessage":null,"card":null,"customer":null,"fraudDetails":{"userReport":null,"stripeReport":null},"status":"succeeded"}}]

## send Thank You email
Req:
curl -i -XPOST 'localhost:4569/donations/7055eb6d-01e2-409d-8435-58c6ae97bcab/thankYou'

Res:
HTTP/1.1 204 No Content
Content-Type: application/json
Server: Jetty(9.0.2.v20130417)