Done:
-lombok integrieren => und als regel festlegen



TODO for MVP:




- introduce empty db / load config via dsl/configuration
- design decision: should an attribute be identity type specific, or should it be separate and share the common definition for all attributes => decision, lets have a base definition (with minimum requirements) and type specific overrides for max length / validation / generation rules / lookups
prompt: currently the AttributeType does link to the IdentityType, i like to enhance the model so that we can have an n:m relationship between the attribute and identity type. Meaning the AttributeType can be defined as separate object, and just mapped to the identity type as part of the identity type definition. Additonally the mapping should allow to have type specific overrides for the validation regex, the default_value. the sort order and the is_required flag should only be present on the mapping table. The overrides must behave cumulative, meaning on validations always both rules need to be evaluated and "matched"
- Add some additional use cases for the attribute values in terms of lifecycle manamgent: AllowSetOnCreate: true/false,DefaultValue:based on groovy script, AllowUpdate:true/false 






- introduce lookups for identity attributes => make it dynamic => either a short list in script, or a table lookup, or a specific entity query. Lets make this script based as well




- introduce identifier type => split identifier username and emailfrom attribute to identifier table
    - add identifier generation service /Api (with generator script to generate identifiers) and allow calling it from identity creation

- introduce ui script editor
- implement logback logging
- implement history tables -> add History View on the UI. Add Auditcolumns for modifiedby,createdby
- implement identity changelog / change Reason  -> extend UI as well to have this entered.

- extend identity with access Rights
- extend identity with credentials -> implement a password checker api, and a password chagne api
- extend identity with login
- extend identity with relation to other identities

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