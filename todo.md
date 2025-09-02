Done:
-lombok integrieren => und als regel festlegen
- design decision: should an attribute be identity type specific, or should it be separate and share the common definition for all attributes => decision, lets have a base definition (with minimum requirements) and type specific overrides for max length / validation / generation rules / lookups
prompt: currently the AttributeType does link to the IdentityType, i like to enhance the model so that we can have an n:m relationship between the attribute and identity type. Meaning the AttributeType can be defined as separate object, and just mapped to the identity type as part of the identity type definition. Additonally the mapping should allow to have type specific overrides for the validation regex, the default_value. the sort order and the is_required flag should only be present on the mapping table. The overrides must behave cumulative, meaning on validations always both rules need to be evaluated and "matched"

- introduce identifier type => very similar to the attribute type i need a separate object for identifierType.i like to have identifiers splittet into a separate path for making the concrete identifier values indexable within the database and therefore can search for an identity over its identifier.




TODO for MVP:




- introduce empty db / load config via dsl/configuration


-introduce caching for identity type definition, which is independet of database. singleton service bean. which is initialized on app-start from database, then regurlarly checks for updates on the database, and if found reloads itself => please use a quartz task for this. additionally allow forced reloading over an admin-frontend ui page. Additionally trigger the definition reloading when the definition is changed over the UI.







- I like to use groovy scripts througout the application. I like to have the scripts stored on the database on a table called SystemConfigScript. On Application Bootup, scripts from the Build should be automatically copied into the table, in case the config from the build is newer than the config on the database and the config on the database still has the same checksum as per the previous build. In case the checksum is not identical, no update on the database should be done and an Alert in the log should be generated, stating that the script couldnt be updated, as it is not in expected state. The table should have a script uuid and a scriptKey, a script Description and a ScriptContent Column. The ScriptKey must be unique, as this will be the key used within the code to reference it. The Scripts mechanism should allow to change and reload the scripts during runtime over an admin-ui component. There should be a configuration on the script, if it should be held in cache or only loaded on the fly when called, as some scripts would be heavily used (where caching is important), while others are seldomely used.


- Add some additional use cases for the attribute values in terms of lifecycle manamgent: AllowSetOnCreate: true/false,DefaultValue:based on groovy script, AllowUpdate:true/false 

- config managemnt:
ok, i would be interested in -> load values from local file, check on database for overrides and apply overrides if necessary. there should be a regular pull to check if any new db override has been made and auto reload, however as well an option to force reload. I like to have the Admin-UI extended with a section to show all Configuration Entries, if they are overriden and allow the values to be changed. The whole configuration should be cached in Memory for fast access throughout the application.




- introduce lookups/validation for identity attributes => make it dynamic => either a short list in script, or a table lookup, or a specific entity query. Lets make this script based as well






    - add identifier generation service /Api (with generator script to generate identifiers) and allow calling it from identity creation

- introduce ui script editor
- implement logback logging
- implement history tables -> add History View on the UI. Add Auditcolumns for modifiedby,createdby
- implement identity changelog / change Reason  -> extend UI as well to have this entered.

- extend identity with access Rights
- extend identity with credentials -> implement a password checker api, and a password chagne api
- extend identity with login
- extend identity with relation to other identities (ownership)

- introduce entity and entitytype

- introduce eventing / changedetection
- Introduce ViewProfiles for IdentityRead

- introduce job system 
- introduce policy framework
- introduce origin on identity (where is it coming from)
- add scim api

- add fine grained security
- add multivalue fields

- introduce sql console / reporting
- introduce bulk changes


- add unit testing
    - prevent updating the identity type on an identity
    - prevent updating the audit columns (created_at, deleted_at, modified_at, modified_by)
- add bulk change commandlets


- work on performance and scalability
    -> add performance tests, creat 10 mio identities ...
- think about preloading / testing maybe crossstaging
- automated documentation
    - adding diagram from Object Model to show relationships

- introduce account model