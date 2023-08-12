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
AND TAGS = {'application' : 'boimama.in'}
---
ALTER TABLE "boimama"."story" ADD TAGS {'environments' : 'development qa production'}
---
SELECT * FROM boimama.story;
SELECT * FROM boimama.story where story_title = 'My first story' ALLOW FILTERING;
SELECT * FROM boimama.story where story_title = 'My first story' and story_category = 'Comedy' ALLOW FILTERING;
SELECT author_names FROM boimama.story WHERE story_id IN (53059326-89e1-445d-9ebd-001b0ad1518a, 308657d4-43a1-40b7-9c06-3738d92ea042) ALLOW FILTERING;
SELECT author_ids FROM boimama.story where story_title = 'My first story' ALLOW FILTERING;
SELECT story_id, story_title, author_ids, author_names FROM boimama.story;
SELECT story_id, story_title, author_ids, author_names FROM boimama.story WHERE story_id=89aca472-884c-4d0d-8a11-0289986d4694;
DELETE FROM boimama.story WHERE story_id=89aca472-884c-4d0d-8a11-0289986d4694;
SELECT story_id, story_title, author_ids, author_names FROM boimama.story WHERE story_id=d4602b31-e41b-4e39-adfc-3ffbdcec5ebf;
DELETE FROM boimama.story WHERE story_id=d4602b31-e41b-4e39-adfc-3ffbdcec5ebf;

---