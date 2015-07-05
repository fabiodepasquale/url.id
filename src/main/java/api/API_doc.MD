
{
"shortUrl": "http://url.id/Ah6J",                       // shortUrl with domain prefix
"longUrl": "http://google.com/",                        // longUrl in url-encoded format
"created": "2009-12-13T07:22:55.000+00:00",             // creating time in ISO8601 format
"analytics": {
    "allTime": {
        "shortUrlClicks": "3227",
        "longUrlClicks": "9358",
        "countries": [ { "count": "1022", "id": "US" } ... ],
        "browsers": [ { "count": "1025", "id": "Firefox" } ... ],
        "platforms": [ { "count": "2278", "id": "Windows" } ... ]
        },
    "month": { ... },
    "week": { ... },
    "day": { ... },
    "twoHours": { ... }
},
"expireTime": "2010-01-13T07:22:55.000+00:00",          // *optional* indicates when short link expires
"expireClicks": "5",                                    // *optional* indicates how many clicks can be done before link expires

"status": "OK",                                         // stay for link message in our service. Can be one of the high-level statuses:
                                                        // OK, EXPIRED, PROTECTED, REMOVED....
                                                        // or any other label that our command can associate to a link* (e.g. MALWARE, ADULT ONLY)
                                                        // *only if high-level message is OK and we can show longUrl

"code": "200",                                          // HTTP message code               } will be provided only
"message": "OK",                                        // Human-readable alternative     } if longUrl is shown
}


{
"error":
    {
    "reason": "required",                               // specifies the error reason. For example, reason will be "required"
                                                        // or "invalid" if some field was missing or malformed

    "message": "Required",                              // human readable explanation of the error
    "locationType": "parameter",                        // if present, specifies what part of the request has an error.
                                                        // Possible values include "parameter" and "header".

    "location": "longUrl"                               // if present, specifies the location in the request that caused the error,
                                                        // e.g. the name of a missing or malformed parameter.
    },
"code": 400,                                          // HTTP message code of this response.
"message": "Required"
}
