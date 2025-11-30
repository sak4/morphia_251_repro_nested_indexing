# Repro for Indexing of Nested Object

Given a nested object structure MyEntity containing Child entity.
When the Child entity has an indexed field.
Then the Child index is not created in parent MyEntity.

In 1.3 version, nested indexes were created. 
