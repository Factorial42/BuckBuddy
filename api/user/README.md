# BuckBuddy User Microservice REST API

## create regular user
Req:
curl -i -XPOST 'localhost:4567/users/signup' -d '{"firstName":"test", "lastName":"user", "email":"test@buckbuddy.com","password":"test"}'

Res:
HTTP/1.1 201 Created
Content-Type: application/json
{"authenticated":true,"deleted":0,"inserted":1,"unchanged":0,"replaced":0,"errors":0,"skipped":0,"token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhMzczMWQyYmQ2ZjJmY2YxMjllNGRmZmZmNDA1NTc5N2RiZTljYWNjYzI1NTc2ZjYyNTYxMjJiZjBhYzM0NTlmIn0.LMXpO016qEH54qi6RmNSi718BDwLGXwxt67QAYmWaYbyslmKW6SocbESWiczB1OZWNKwlqf1pkqBinRuejwWvg"}

## login regular user
Req:
curl -i -XPOST 'localhost:4567/users/login' -d '{"email":"test@buckbuddy.com", "password":"test"}'

Res:
{"userId":"a3731d2bd6f2fcf129e4dffff4055797dbe9caccc25576f6256122bf0ac3459f","token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhMzczMWQyYmQ2ZjJmY2YxMjllNGRmZmZmNDA1NTc5N2RiZTljYWNjYzI1NTc2ZjYyNTYxMjJiZjBhYzM0NTlmIn0.rA6sL7-25vEOq4-NLe-0y5rlzzc_gSQLHxRDdA0vM4KnHfq5h29iFL_X6Cpf19HUb2aDo4xV-wTpKpwV8E_gew","authenticated":true,"createdAt":1460486525.930000000,"lastUpdatedAt":1460486526.562000000,"firstName":"test","lastName":"user","email":"test@buckbuddy.com","password":"9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08","s3handle":"ef19581ea5f645288f1e27b5c412c6e5","active":true}J

## fb get profile
Req:
curl -XGET 'localhost:4567/users/fb/profile?code=AQBtgqbivQlGBVF1zqDhktGgNCcwWnAhE5lC-hsoA9GuKSz0uwf19mikWivNuQIHOQRm9jFkXOa8HccOhgbKHa140xcP3OvjgMapTdCfs-JLya32neUJTf50q2N4WiSOb9NXKgNw97q58dnJZH4wlDqAH68qYtsvaoJa6V1_sJjp34MTqJ4PJmP3mG8UtX_jU36teNn7ol-qZK3pvh7CMq3jDUrM0Jn6Z4qKHH3o63NQwloFR-5CxsTsJQQ_dT-VJrlWkLsgaTuAaNJFhxb8gkLITtXiUx_UY03wzg4VOqNtMXH1DqGgzUSZPWI4P3FwuiOB5v65nocb8-yMuu05Gpu4noY5GW-N2VyjSzUbDcsvzQ'

Res:
{"firstName":"Bucky","middleName":"One","lastName":"Tester","socialProfiles":{"facebookProfile":{"name":"Bucky One Tester","facebookID":"gT8evSo8fJiiZEeEjhnfaIQdyE4","pic":"https://scontent.xx.fbcdn.net/hprofile-xfa1/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=de3cf921c79d63ac0e908353690899f6&oe=5785D22F","firstName":null,"middleName":null,"lastName":null,"email":"bucky_ykqhoax_tester@tfbnw.net","userAccessToken":"CAAVwXiHXUp0BAIPgNJhjEnjUi1jKx69I8evPpctF9a1VxhD1bEi5IZCjqfgDZCkZBmrPa1d78VW3w9l8RSvqXCQtcDQT49sVfWZCRT68ZC3LgpZBlZAVaBlZAxpgAg6Hu6lWNWuVN7dr4bWtxvTFELVeHXkPd036whElq5m3iXoVoCDP8ENoqIF0X0imLrpYuecZD","appAccessToken":null,"pageAccessToken":null,"clientToken":null},"twitterProfile":null,"whatsappProfile":null,"smsProfile":null}}

## fb signup
Req:
curl -i -XPOST 'localhost:4567/users/fb/signup' -d '{"email":"bucky_ykqhoax_tester@tfbnw.net", "firstName":"Bucky","middleName":"One","lastName":"Tester","socialProfiles":{"facebookProfile":{"name":"Bucky One Tester","facebookID":"gT8evSo8fJiiZEeEjhnfaIQdyE4","pic":"https://scontent.xx.fbcdn.net/hprofile-xfa1/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=de3cf921c79d63ac0e908353690899f6&oe=5785D22F","firstName":null,"middleName":null,"lastName":null,"email":null,"userAccessToken":"CAAVwXiHXUp0BAAygkvbrZBT4E91TOkcYsZA3VCtFemqkGVfMHxKdMjh7zwe0mFwuZBgPf48MoAIUVlHvmJXXa13Gg6yZA8CoABo7pWxIW49vZATKG1ZAW7DSd9ayZB7yKfoqhw9Bxwm0JDUMhowwI6e9tN4jmEwOfsKAJp0m2Lejm0ZAxtLjExIvdgZBFBTc9YZAQZD","appAccessToken":null,"pageAccessToken":null,"clientToken":null, "email":"bucky_ykqhoax_tester@tfbnw.net"},"twitterProfile":null,"whatsappProfile":null,"smsProfile":null}}'

Res:
HTTP/1.1 201 Created
Content-Type: application/json
{"authenticated":true,"deleted":0,"inserted":1,"unchanged":0,"replaced":0,"errors":0,"skipped":0,"token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3ODIxNTk3MDhlOWJiM2UzYWY1YzliZGYxZmY3N2Y3MDgyMzQxOGQxNjk3MGViODJmZTRkN2UxY2E1Y2E2OWFjIn0.mKQeq7mNQodV-ZV1Epg5gPihPN8EAygEAGvmGPHhg68tlxkwo6gQN7jLXQVNH7tSwnDdy-E6LbFxz08Z7fOayg"}

## fb login
Req:
curl -i -XPOST 'localhost:4567/users/fb/login?code=AQCMvR6DltrmRi_s0Y1pOn4IziTTdy-_Ani5r8fmxtFBJLHrDllLhK5SExESgi9DQEIZOpaHifatI5vq0UiyyMtdeNMWGPtAbAbQ3CC6TLm0v05D9JWF4cK8r8Hm2_Wta5x_PrSPHiFNRltUCIvn9_-xO4DcaDl5NwIcxtkF24bX3-A0qkGlFRIotcILp6vouhp5ydw6VWmdnD2yl43GZVfhNhXRwnfgv8b1owp8M39Z1a4TTsqWJ7YbG0wP0Jpw2YvEyTijsZA1qXZs3IsPC7CKnLmRStvKLDcEKnCSH7Om899kh_lpgFbbw9yIOhdckaE55BruDqxM9726vnlFDQFcjjM8SsYabzjdFOly-uCFOA'

Res:
{"userId":"782159708e9bb3e3af5c9bdf1ff77f70823418d16970eb82fe4d7e1ca5ca69ac","token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3ODIxNTk3MDhlOWJiM2UzYWY1YzliZGYxZmY3N2Y3MDgyMzQxOGQxNjk3MGViODJmZTRkN2UxY2E1Y2E2OWFjIn0.mKQeq7mNQodV-ZV1Epg5gPihPN8EAygEAGvmGPHhg68tlxkwo6gQN7jLXQVNH7tSwnDdy-E6LbFxz08Z7fOayg","authenticated":true,"createdAt":1460499042.676000000,"lastUpdatedAt":1460499043.242000000,"firstName":"Bucky","middleName":"One","lastName":"Tester","email":"bucky_ykqhoax_tester@tfbnw.net","s3handle":"7d913c52511e1ad37d82acb1573b5b39","socialProfiles":{"facebookProfile":{"name":"Bucky One Tester","facebookID":"gT8evSo8fJiiZEeEjhnfaIQdyE4","pic":"https://scontent.xx.fbcdn.net/hprofile-xfa1/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=de3cf921c79d63ac0e908353690899f6&oe=5785D22F","firstName":null,"middleName":null,"lastName":null,"email":"bucky_ykqhoax_tester@tfbnw.net","userAccessToken":"CAAVwXiHXUp0BAAygkvbrZBT4E91TOkcYsZA3VCtFemqkGVfMHxKdMjh7zwe0mFwuZBgPf48MoAIUVlHvmJXXa13Gg6yZA8CoABo7pWxIW49vZATKG1ZAW7DSd9ayZB7yKfoqhw9Bxwm0JDUMhowwI6e9tN4jmEwOfsKAJp0m2Lejm0ZAxtLjExIvdgZBFBTc9YZAQZD","appAccessToken":null,"pageAccessToken":null,"clientToken":null},"twitterProfile":null,"whatsappProfile":null,"smsProfile":null},"active":true}