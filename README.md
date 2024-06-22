# Project Overview

This project provides a relatively simple and lightweight solution to the following problem:
How can a frontend perform complex and dynamic queries to a backend?

### Detailed requirements:
- By complex and dynamic, I mean queries that can be performed on multiple fields at once, possibly across several levels of nested entities, and that handle various operators.
- The aim is to offer capabilities that are similar to SQL without allowing users to execute raw database queries, ensuring a satisfactory level of security.
- Both components are aware of the data model, but the goal is to avoid writing endless boilerplate for each possible query.
- The queries will be initiated by an independent frontend, so it is necessary to be able to pass the components of the queries to a REST API.

### Existing solutions:
- RSQL does not support complex queries with relations and its ecosystem is aging.
- A simple use of Controller would require a lot of boilerplate as each combination of conditions would need to be individually managed.
- Spring Data REST would not allow for handling complex and dynamic queries natively.
- GraphQL also does not offer this capability.

## Solution

The implementation is located in the `sb_querydsl_criteria_builder` directory, which is an independent Maven module.
The solution relies on `querydsl` to dynamically create queries from a custom `SearchCriterion` model.
`CriteriaQueryBuilder` is the main class and allows creating a `JPAQuery` from a list of `SearchCriterion`.
Each `SearchCriterion` has a property with a field, an operator, and either a value or a nested list of `SearchCriterion` allowing the execution of subqueries. The idea is to represent with the same model both simple comparison operations (equals/not equals/greater than/etc.) and subqueries (exists/not exists) that allow recursively constructing complex queries on linked entities.

Given that the query originates from an independent frontend and are represented as JSON, all input elements are strings, which had to be managed.
For this, the backend must implement a configuration class: `EntityPathResolver`. This class contains a map with strings as keys (which will be the name of the fields entered) and the auto-generated QClass as values. Typically, you'll find `"library": QLibrary.library`. This allows both mapping from strings and controlling the accessible entities.

### Classes:
- `CriteriaQueryBuilder` is the entry point that generates the query from the criteria and recursively processes potential subqueries.
- `DynamicFieldCaster` casts the values passed, these values are also passed as strings in the JSON, so they need to be dynamically cast and manage multiple possible types: number, date, collection, etc.
- `ExpressionBuilder` generates the multiple `BooleanExpression` corresponding to each criterion. The complexity here lies in supporting comparison operators for several types/families of types.
- `EntityPathResolver` is the configuration class mentioned above that allows referencing accessible QClasses in query creation and associating them with their string names.

## Project Structure

Aside from `querydsl-criteria-builder`, which is the core of the implementation and can be used entirely independently, two other components are present as a proof of concept:

### test-backend
A minimalist Spring Boot service exploits `querydsl-criteria-builder`. For example, it has a rich data model with a parent entity Library and a set of relations with Books, Users, etc.
It exposes an endpoint `/api/v1/library/query` that allows searching its data model.

### test-frontend
A Vue.JS project that completes the proof of concept. It has a single page where the user can dynamically add/remove fields to their query.
For the example, I stopped at one level of depth in entity relations (Library > Book), and the search result displays the corresponding libraries in a simple table without additional visualization functionality.
Bonus feature: The current query dynamically registers in query params, allowing for sharing and copy/pasting a URL that will directly display the query.

## Testing

- Run `docker-compose up`.
- For example, you can use this pre-filled query:  
  http://localhost:4173/?criteria=W3siZmllbGQiOiJuYW1lIiwib3AiOiJsaWtlIiwidmFsdWUiOiJsaWIifSx7ImZpZWxkIjoiZXN0YWJsaXNoZWREYXRlIiwib3AiOiJsdCIsInZhbHVlIjoiMjAyMy0wOS0wNSJ9LHsiZmllbGQiOiJib29rIiwib3AiOiJleGlzdHMiLCJzdWJRdWVyeSI6dHJ1ZSwic3ViQ3JpdGVyaWEiOlt7ImZpZWxkIjoicHVibGlzaFllYXIiLCJvcCI6ImluIiwic3ViUXVlcnkiOmZhbHNlLCJ2YWx1ZSI6IjE5MzAsIDE5NDAsIDE5NTAifSx7ImZpZWxkIjoiZ2VucmUiLCJvcCI6Im5lIiwic3ViUXVlcnkiOmZhbHNlLCJ2YWx1ZSI6IkZhbnRhc3kifSx7ImZpZWxkIjoiZWRpdGlvbiIsIm9wIjoibm90bGlrZSIsInN1YlF1ZXJ5IjpmYWxzZSwidmFsdWUiOiJzZWNvbmQifV19LHsiZmllbGQiOiJib29rIiwib3AiOiJub3RleGlzdHMiLCJzdWJRdWVyeSI6dHJ1ZSwic3ViQ3JpdGVyaWEiOlt7ImZpZWxkIjoibGFuZ3VhZ2UiLCJvcCI6ImVxIiwic3ViUXVlcnkiOmZhbHNlLCJ2YWx1ZSI6IlJ1c3NpYW4ifV19XQ==
- Launch the query from the frontend, and the backend will respond with the corresponding libraries.
