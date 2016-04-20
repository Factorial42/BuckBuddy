# BuckBuddy User Microservice REST API

## General notes:
Response structure will have "data"-holding user object for this service and "error"-holding any "message" specific to errors that occured in the backend elements.

## create regular user
Req:
curl -i -XPOST 'localhost:4567/users/signup' -d '{"firstName":"test", "lastName":"user", "email":"test1@buckbuddy.com","password":"test"}'

Res: User object with token
HTTP/1.1 201 Created
Date: Wed, 13 Apr 2016 08:06:58 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"data":{"userId":"ec67978322e5fbd7604e8db2ad0e89ab1fc4b7dee0a9a9655fb662b8ae647e71","token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlYzY3OTc4MzIyZTVmYmQ3NjA0ZThkYjJhZDBlODlhYjFmYzRiN2RlZTBhOWE5NjU1ZmI2NjJiOGFlNjQ3ZTcxIn0.gAarGBEnQf11oc8h2bfyaiLoBYGOiaj_Lrjb-KV_SL5GZECE7j50wegLkI7ea1RfNAZBhFFa6LV4IPJQ7I3rjA","createdAt":1460534818.875,"lastUpdatedAt":1460534818.875,"firstName":"test","lastName":"user","email":"test1@buckbuddy.com","s3handle":"d58415bbb7da062cb020a2965bb862f3","active":true},"error":null,"userSlug":"test-user-1461137856"}

## create regular user - error 
Res:
HTTP/1.1 401 Unauthorized
Date: Wed, 13 Apr 2016 08:07:51 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"data":null,"error":{"message":"Duplicate primary key.."}}

## Get user by token
curl -XGET 'localhost:4567/users/byToken/eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjMzYzNGI5MTU0MzgwMmZmYjYxMzk1ZDkxYTgzYWViYTU0ZTEyMGRiMmZlYmI4ZmFkN2M5YjEzZGViOTcxYTEyIn0.m0BiopdId3pf-mTpIeztMSPBHm41-cYVAY-Qhaj4Q4cEAkPOz4cdJrlr4pAgs9xvtFtre8hr7rNJEo_Jbl1GYg'

## login regular user
Req:
curl -i -XPOST 'localhost:4567/users/login' -d '{"email":"test1@buckbuddy.com", "password":"test"}'

Res: User object with token
HTTP/1.1 200 OK
Date: Wed, 13 Apr 2016 08:09:22 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"data":{"userId":"ec67978322e5fbd7604e8db2ad0e89ab1fc4b7dee0a9a9655fb662b8ae647e71","token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlYzY3OTc4MzIyZTVmYmQ3NjA0ZThkYjJhZDBlODlhYjFmYzRiN2RlZTBhOWE5NjU1ZmI2NjJiOGFlNjQ3ZTcxIn0.gAarGBEnQf11oc8h2bfyaiLoBYGOiaj_Lrjb-KV_SL5GZECE7j50wegLkI7ea1RfNAZBhFFa6LV4IPJQ7I3rjA","createdAt":1460534818.875,"lastUpdatedAt":1460534818.875,"firstName":"test","lastName":"user","email":"test1@buckbuddy.com","s3handle":"d58415bbb7da062cb020a2965bb862f3","active":true},"error":null,"userSlug":"test-user-1461137856"}

## fb get profile
Req:
curl -i -XGET 'localhost:4567/users/fb/profile?fbToken=AQBVwziF2tuwnvRu0J9ypm0ld70o0sAjpJUUwhcNCBW_Gq6tNpeh9iQ9on8XSVlgmbkx0BfEuXHs2qC3v6v8mx7SQcYTGRSIAJfvTnOD4iYGjxe7xaqALudRrQzITD_yDVfstskrpm_oyfYFvRttEqGkZIDPp56MoD7Rfge2CPCZInJlRdYup-aNqTs3P40w4zsm_RZKVWdVVoMJnI0KBS1j7Wy4Il2CjQEGVXtpktbaCaECHBiePDab889gliyw3Dq87WzlUNteCFcG_WPeEkUCHx9TTSFwFdTf28Io7t2elmzx_A6t_f_bUHFuzoyOST5oMUA0G51e0P6xOxiO022yrvOSU2sTqc13ZRLXgrXJFQ'

Res: User object with token, facebook profile info
TTP/1.1 200 OK
Date: Wed, 13 Apr 2016 08:23:37 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"data":{"firstName":"Bucky","middleName":"One","lastName":"Tester","socialProfiles":{"facebookProfile":{"name":"Bucky One Tester","facebookID":"gT8evSo8fJiiZEeEjhnfaIQdyE4","pic":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=de3cf921c79d63ac0e908353690899f6&oe=5785D22F","firstName":null,"middleName":null,"lastName":null,"email":"bucky_ykqhoax_tester@tfbnw.net","userAccessToken":"CAAVwXiHXUp0BAECd2oDJ4z4qEs1Ci2fF3y2ZBvIuECSeUG6WfoSFpuVubaZA9ZC6kcJUZBW5leNSHvZC1fCfK6ZAVxlgQGElQn6ZAnjZB5SlGace7iRK9xZAw9ozseZA4wY4h607LXWZBF65dDHmhll68eKUVOuzu1aatLmI1ybGlX6sqZC5clb2tqydmFyulhZCPbskZD","appAccessToken":null,"pageAccessToken":null,"clientToken":null},"twitterProfile":null,"whatsappProfile":null,"smsProfile":null}},"error":null,"userSlug":"test-user-1461137856"}

## fb signup
Req:
curl -i -XPOST 'localhost:4567/users/fb/signup' -d '{"email":"bucky_ykqhoax_tester@tfbnw.net", "name":"Bucky","fbToken":"AQDgwh37EPfqzgh3TizPO4xex3vSHTu5M6TV84ehyN3V3aPn3bj5I5uXeCswj4lRy6rHCbcJjWUYgUdDlm9-WePf3RfMWf3syIwG7BlTnNOMdo4scdoyjbr-nvILHQZjjbaLEpuY5dETOZ9Tpvqg2AE7lLWxGDd5T6oh198vTab8Dk1r5xiBaWxre4vxk99s6ncb3RimXIBVXaVVhXb0RGf_32OgRZkjysSbfKeTWUNflabhZECQRygZbkxuy7h-hkv5Ay7uHb0jC8FPuOfC-mDlRGNBV9Juo7o0D1E2EFgAuIS6FjhIGdDKBHMqe_VvJdCVEYLVItS4Rn5rXQ4NsnWe"}'

Res: User object with token, facebook profile
HTTP/1.1 201 Created
Date: Wed, 13 Apr 2016 22:41:45 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"data":{"userId":"782159708e9bb3e3af5c9bdf1ff77f70823418d16970eb82fe4d7e1ca5ca69ac","token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3ODIxNTk3MDhlOWJiM2UzYWY1YzliZGYxZmY3N2Y3MDgyMzQxOGQxNjk3MGViODJmZTRkN2UxY2E1Y2E2OWFjIn0.mKQeq7mNQodV-ZV1Epg5gPihPN8EAygEAGvmGPHhg68tlxkwo6gQN7jLXQVNH7tSwnDdy-E6LbFxz08Z7fOayg","createdAt":1460587372.428,"lastUpdatedAt":1460587372.438,"name":"Bucky","firstName":"Bucky","middleName":"One","lastName":"Tester","email":"bucky_ykqhoax_tester@tfbnw.net","s3handle":"7d913c52511e1ad37d82acb1573b5b39","socialProfiles":{"facebookProfile":{"name":"Bucky One Tester","facebookID":"gT8evSo8fJiiZEeEjhnfaIQdyE4","pic":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=1b2f428689c126f80a083e30205ef68b&oe=57AD5F2F","firstName":null,"middleName":null,"lastName":null,"email":"bucky_ykqhoax_tester@tfbnw.net","userAccessToken":"CAAVwXiHXUp0BAHbdH42Dd7XNwssZBn9fO6jtxetxmvrBWhGaQdciybMe7aD7mkdZC1ceqaW6FjaJ3e4hm51hprrIY74bVMQu230rIbIH38FcG4aiOHYfHD4ZCDi3GRZBxhg1JPPFIq3Wolrh94dy5RVk8hLVxZBCH5ZAw7dsTUl552xe1OFZC8gmrznts8EYFMZD","appAccessToken":null,"pageAccessToken":null,"clientToken":null},"twitterProfile":null,"whatsappProfile":null,"smsProfile":null},"active":true,"userSlug":"test-user-1461137856"}}

## fb login
Req:
curl -i -XPOST 'localhost:4567/users/fb/login?fbToken=AQCMvR6DltrmRi_s0Y1pOn4IziTTdy-_Ani5r8fmxtFBJLHrDllLhK5SExESgi9DQEIZOpaHifatI5vq0UiyyMtdeNMWGPtAbAbQ3CC6TLm0v05D9JWF4cK8r8Hm2_Wta5x_PrSPHiFNRltUCIvn9_-xO4DcaDl5NwIcxtkF24bX3-A0qkGlFRIotcILp6vouhp5ydw6VWmdnD2yl43GZVfhNhXRwnfgv8b1owp8M39Z1a4TTsqWJ7YbG0wP0Jpw2YvEyTijsZA1qXZs3IsPC7CKnLmRStvKLDcEKnCSH7Om899kh_lpgFbbw9yIOhdckaE55BruDqxM9726vnlFDQFcjjM8SsYabzjdFOly-uCFOA'

Res: User object with token, facebook profile
HTTP/1.1 200 OK
Date: Wed, 13 Apr 2016 08:27:59 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{"data":{"userId":"782159708e9bb3e3af5c9bdf1ff77f70823418d16970eb82fe4d7e1ca5ca69ac","token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3ODIxNTk3MDhlOWJiM2UzYWY1YzliZGYxZmY3N2Y3MDgyMzQxOGQxNjk3MGViODJmZTRkN2UxY2E1Y2E2OWFjIn0.mKQeq7mNQodV-ZV1Epg5gPihPN8EAygEAGvmGPHhg68tlxkwo6gQN7jLXQVNH7tSwnDdy-E6LbFxz08Z7fOayg","authenticated":true,"createdAt":1460535981.233,"lastUpdatedAt":1460535981.233,"firstName":"Bucky","middleName":"One","lastName":"Tester","email":"bucky_ykqhoax_tester@tfbnw.net","s3handle":"7d913c52511e1ad37d82acb1573b5b39","socialProfiles":{"facebookProfile":{"name":"Bucky One Tester","facebookID":"gT8evSo8fJiiZEeEjhnfaIQdyE4","pic":"https://scontent.xx.fbcdn.net/hprofile-xfa1/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=de3cf921c79d63ac0e908353690899f6&oe=5785D22F","firstName":null,"middleName":null,"lastName":null,"email":"bucky_ykqhoax_tester@tfbnw.net","userAccessToken":"CAAVwXiHXUp0BAAygkvbrZBT4E91TOkcYsZA3VCtFemqkGVfMHxKdMjh7zwe0mFwuZBgPf48MoAIUVlHvmJXXa13Gg6yZA8CoABo7pWxIW49vZATKG1ZAW7DSd9ayZB7yKfoqhw9Bxwm0JDUMhowwI6e9tN4jmEwOfsKAJp0m2Lejm0ZAxtLjExIvdgZBFBTc9YZAQZD","appAccessToken":null,"pageAccessToken":null,"clientToken":null},"twitterProfile":null,"whatsappProfile":null,"smsProfile":null},"active":true},"error":null,"userSlug":"test-user-1461137856"}

## Pic upload
Req:
curl -XPOST 'localhost:4567/users/782159708e9bb3e3af5c9bdf1ff77f70823418d16970eb82fe4d7e1ca5ca69ac/uploadProfilePic'  -F "filecomment=This is an image file" -F "image=@/Users/jtandalai/Downloads/Fresh_Brothers_65x35.png"

Res:
HTTP/1.1 100 Continue

HTTP/1.1 200 OK
Date: Fri, 15 Apr 2016 17:14:45 GMT
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.3.2.v20150730)

{}

## Get Access Token and update Stripe Payment profile for user
Req:
curl -i -XPOST 'localhost:4567/users/782159708e9bb3e3af5c9bdf1ff77f70823418d16970eb82fe4d7e1ca5ca69ac/paymentProfile?code=ac_8HN3KMoW6NHdwvCZuWEtBBtbySE8wGeT'

Res:
HTTP/1.1 204 No Content
Date: Sat, 16 Apr 2016 07:57:41 GMT
Content-Type: application/json
Server: Jetty(9.3.2.v20150730)


