curl 'http://localhost:8080/brands/001' -X PATCH -H "Content-Type: application/json" -d '{
  "name" : "new_name"
}'