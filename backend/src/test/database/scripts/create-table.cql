CREATE TABLE "boimama"."story"(
	"id" uuid,
	"story_title" text,
	"published_date" date,
	"story_category" text,
	"story_content" text,
	"story_description" text,
	"story_image" blob,
	"story_image_path" text,
	"story_length_in_mins" int,
	"story_rating" int,
	"author_ids" list<uuid>,
	"author_names" list<text>,
	PRIMARY KEY(("id", "story_title"), "published_date"))
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
AND CLUSTERING ORDER BY("published_date" ASC)
AND TAGS = {'application' : 'boimama.in'}
---
ALTER TABLE "boimama"."story" ADD ("story_image_path" text);
---

CREATE TABLE "boimama"."user"(
	"username" text,
	"fname" text,
	"lname" text,
	PRIMARY KEY("username"))
WITH default_time_to_live = 1209600 AND CUSTOM_PROPERTIES = {
	'capacity_mode':{
		'throughput_mode':'PAY_PER_REQUEST'
	},
	'point_in_time_recovery':{
		'status':'enabled'
	},
	'encryption_specification':{
		'encryption_type':'AWS_OWNED_KMS_KEY'
	}
} AND TAGS = {'application' : 'boimama.in'}