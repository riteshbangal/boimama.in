CREATE TABLE "boimama"."story"(
	"id" uuid,
	"story_name" text,
	"story_description" text,
	"published_date" date,
	"author_names" list<text>,
	"author_ids" list<uuid>,
	"story_image" blob,
	"story_content" text,
	"story_rating" int,
	"story_category" text,
	"story_title" text,
	"story_length_in_mins" int,
	PRIMARY KEY("id", "story_name"))
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
};

ALTER TABLE "boimama"."story" ADD TAGS {'application' : 'boimama.in'};
