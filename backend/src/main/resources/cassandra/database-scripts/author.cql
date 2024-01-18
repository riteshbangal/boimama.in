CREATE TABLE "boimama"."author"(
	"author_id" uuid,
	"author_name" text,
	"user_name" text,
	"biography" text,
    "followers" int,
    "published_works" list<text>,
	"contact_details" text,
	"joining_date" date,
	"author_image" blob,
	"author_image_path" text,
	"version" int,
	PRIMARY KEY(("author_id"), "version"))
WITH CUSTOM_PROPERTIES = {
	'capacity_mode':{
		'throughput_mode':'PAY_PER_REQUEST'
	},
	'point_in_time_recovery':{
		'status':'enabled'
	},
	'encryption_specification':{
		'encryption_type':'AWS_OWNED_KMS_KEY'
	}
}
AND CLUSTERING ORDER BY("version" ASC)
AND TAGS = {'application' : 'boimama.in'}
---
ALTER TABLE "boimama"."author" ADD TAGS {'environments' : 'development qa production'}
---
SELECT * FROM boimama.author;
SELECT * FROM boimama.author where author_name = 'Ritesh' ALLOW FILTERING;
---