# Liowl-API

:books: API Liowl do projeto de bibliotecas

| Método |                                       | Content                                               | Success          | Error | Observação |
|--------|---------------------------------------|-------------------------------------------------------|------------------|-------|------------|
| POST   | /api/books                            | {"title":"string", "author":"string", "isbn":"string} | CREATED (201)    |       |            |
| PUT    | /api/books/{id}                       | {"title":"string", "author":"string", "isbn":"string} | OK (200)         |       |            |
| GET    | /api/books/{id}                       |                                                       | OK (200)         |       |            |
| GET    | /api/books?title=''&author=''&isbn='' |                                                       | OK (200)         |       |            |
| DELETE | /api/books/{id}                       |                                                       | NO CONTENT (204) |       |            |
