## Words Frequency Evaluator Service
This is a simple service that allows you to send a string of comma-separated words and get statistics about the frequency of those words. 
The service has two endpoints:
 - Add Words Endpoint: Accepts a string of comma-separated words and adds them to the storage.
 - Statistics Endpoint: Returns statistics about the frequency of all words received so far, including the top 5 recurring words, 
the minimum frequency among all words, and the median frequency.

The service is documented using Swagger UI, and you can access the API documentation at /swagger-ui
