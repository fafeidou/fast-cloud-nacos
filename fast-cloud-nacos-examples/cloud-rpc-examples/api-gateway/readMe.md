```

{
    "routeDefinitionList": [
        {
            "filters": [
                {
                    "args": {
                        "_genkey_0": "1"
                    }, 
                    "name": "StripPrefix"
                }
            ], 
            "id": "service-hi", 
            "order": 0, 
            "predicates": [
                {
                    "args": {
                        "pattern": "/service-hi/**"
                    }, 
                    "name": "Path"
                }
            ], 
            "uri": "lb://service-hi"
        }, 
        {
            "filters": [ ], 
            "id": "jd_route", 
            "order": 0, 
            "predicates": [
                {
                    "args": {
                        "pattern": "/jd"
                    }, 
                    "name": "Path"
                }
            ], 
            "uri": "http://www.baidu.com"
        }
    ]
}

```