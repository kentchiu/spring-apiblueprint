+ Request (application/json)

			{
			  "no" : "001",
			  "name" : "我爱我家"
			}

+ Response 400 (text/plain;charset=ISO-8859-1)

			{"globalErrors":[],"fieldErrors":[{"codes":["Duplicated.Brand.no","Duplicated.no","Duplicated.java.lang.String","Duplicated"],"arguments":["001"],"defaultMessage":"the value of %s key is duplicated","objectName":"Brand","field":"no","rejectedValue":"001","bindingFailure":false,"code":"Duplicated"}]}