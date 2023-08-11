CREATE TABLE "boimama"."story"(
	"story_id" uuid,
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
	PRIMARY KEY(("story_id"), "story_title"))
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
AND CLUSTERING ORDER BY("story_title" ASC)
AND TAGS = {'application' : 'boimama.in'}
---
ALTER TABLE "boimama"."story" ADD TAGS {'environments' : 'development qa production'}
---
SELECT * FROM boimama.story;
---