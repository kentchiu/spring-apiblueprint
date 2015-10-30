## TODO
- 已render的文件要可以被其他文件include
- GET method的url parameters
- PUT url 參數沒出現在 curl之中 ex: param("foo","bar"), `curl /resources/uuid?foo=bar` 中要出現`foo=bar`，但是沒有出現
- 日期format會變成emoji (`:s -> smile`)

## DONE
- attribute 裡的object 改成 name，~~如果可以，加上link，但要考慮可能會有跨頁問題~~
- table content alignment
- endpoint, {endpoint}
- response要支援utf-8，目前中文會亂碼 (http://localhost:3000/#目錄模版管理-目錄模版-delete)
- ~~`CatalogueTemplateControllerTest/testDeleteCatalogueTemplate_catalogue_only-curl.md` 裡面的catalogue_only沒有在curl裡~~ can't reproduce
- attribute table 加上 table.column

