Feature: Process Example
  Scenario Outline: send get,then send post
    Given initialize test data keys "<keys>" values "<values>":

#    first request
    And the headers are:
      | Name | Value |
      |  Content-Type |  application/x-www-form-urlencoded  |
      |   Connection  |          keep-alive             |

    And add header value:
      | Name | Value |
      |  Accept | */*  |
    And the request url is :
      """
    /get
    """
   And the request params are:
      | ParamsKey | ParamsValue |
      |  test1    | valueForStore   |
      |  {test1}  | {test2}|
    When I send a GET request
    Then the response status should be "<code>"
    And store the JSON response "<jsonPath>" as "<storeName>"

#    second request
    And the request url is :
    """
    /post
    """
    And the request params are:
      | ParamsKey | ParamsValue |
      |  storeKey    | {store1}   |
    And the postString is:
    """
    {store1}
    """
    When I send a POST request
    Then the response status should be "<code>"



    Examples:
      | keys  | values|  code |jsonPath|storeName|
      |<test1><test2>|<456><efg> |200| $.args.test1|store1|