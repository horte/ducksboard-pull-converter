ducksboard-pull-converter
=========================

`http://ducksboard-pull-converter.herokuapp.com`  
Example: `http://ducksboard-pull-converter.herokuapp.com/?url=[URL-TO-JSON-CONTENT]&jsonpath=$.['com.yammer.metrics.web.WebappMetricsFilter'].requests.rate.mean`


Converts any json field to a Ducksboard value

```
{
"timestamp": 1386867127,
"value": 4028104
}
```


Takes to 2 params  

url: the url to where the json exists  

jsonpath: the jsonpath for the value to extract (see https://github.com/jayway/JsonPath)  



