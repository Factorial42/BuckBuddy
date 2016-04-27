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