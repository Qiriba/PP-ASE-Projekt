# How to run
Clone repo, Mit IntelliJ Idea Projekt/.idea/runConfigurations/BankingApplication.xml als Konfiguration verwenden.


# WORKFLOW

## Register
POST http://localhost:8080/customers/register
Content-Type: application/json

{
"password": "123123",
"pin": "1231",
"username": "abcde"
}
-> Dies erstellt einen neuen Nutzer. Username muss unique sein




## Login
POST http://localhost:8080/auth/login
Content-Type: application/json

{
"Username": "abcde",
"Password": "123123"
}
-> returnt einen JWT, dieser sollte in folgenden aufrufen verwendet werden



## Account Creation

POST http://localhost:8080/api/accounts
Content-Type: application/json
Authorization: Bearer Your-JWT

{
"currencyCode": "EUR",
"initialDeposit": 69
}

Gibt AccountId des erstellten Accounts zurück 

## Deposit


POST http://localhost:8080/api/banking/deposit
Content-Type: application/json
Authorization: Bearer Your-JWT

{
"accountId": "accountid die dir gehört",
"amount": 100,
"currency": "EUR"
}


## Transfer


POST http://localhost:8080/api/banking/transfer
Content-Type: application/json
Authorization: Bearer Your-JWT
{
"fromAccountId": "Account you owm",
"toAccountId": "Any",
"amount": ,
"currency": "EUR"
}


## Withdraw

POST http://localhost:8080/api/banking/withdraw
Content-Type: application/json

{
"amount": ,
"currency": "EUR"",
"accountId": "Account you Own"
}
